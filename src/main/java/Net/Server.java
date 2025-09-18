package Net;

import Classes.Command;
import Classes.MusicBand;
import Commands.CheckUser;
import Commands.Load;
import Commands.Save;
import DB.DbStuff;
import Exceptions.NotReceivedException;
import Managers.CollectionManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

public class Server {
    public static Logger logger = Logger.getLogger("logger");

    // Threading resources
    private static final ForkJoinPool readPool = new ForkJoinPool();
    private static final ExecutorService processPool = Executors.newCachedThreadPool();

    // Read-write lock for synchronizing access to the collection
    private static final ReadWriteLock collectionLock = new ReentrantReadWriteLock();

    static CollectionManager collectionManager;

    public static void main(String[] args) {
        logger.info("Инициализация сервера...");

        collectionManager = new CollectionManager();

        logger.info("Загрузка данных коллекции...");

        String cPath = "";

        try {
            cPath = args[0];
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            logger.info("Сервер запущен с пустой коллекцией");
        }

        DbStuff.connectToDB();

        logger.info(new Load().execute(collectionManager, new String[]{null, cPath}, 0));

        String path = cPath;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Сервер отключен\n" + new Save().execute(collectionManager, new String[]{null, path}, 0));

            // shutdown thread pools
            readPool.shutdownNow();
            processPool.shutdownNow();
        }));

        int port = 12345;
        boolean running = true;

        try (
                ServerSocket serverSocket = new ServerSocket(port);
        ) {
            logger.info("Сервер запущен, порт: " + port);

            while (running) {

                Socket clientSocket = getClientSocket(serverSocket);
                clientSocket.setSoTimeout(200);

                // handle each client in its own short-lived handler running on the processPool
                processPool.submit(() -> handleClient(clientSocket, collectionManager));

                // main thread continues to accept new connections
            }
        } catch (IOException e) {
            logger.warning("Ошибка в главном потоке сервера: " + e.getMessage());
        }
    }

    static Socket getClientSocket(ServerSocket serverSocket) throws IOException {
        Socket clientSocket = serverSocket.accept();
        logger.info("Клиент подключен: " + clientSocket.getInetAddress());
        return clientSocket;
    }

    static void handleClient(Socket clientSocket, CollectionManager collectionManager) {
        boolean running = true;
        try (
                InputStream in = clientSocket.getInputStream();
                OutputStream out = clientSocket.getOutputStream();
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            while (running && !clientSocket.isClosed()) {
                // allow console commands to shutdown server if this is the server main console
                // Note: consoleReader.ready() will usually be false here; this is kept for parity with original logic
                try {
                    if (consoleReader.ready()) {
                        String command = consoleReader.readLine();
                        if ("exit".equalsIgnoreCase(command.trim())) {
                            logger.info("Завершение работы сервера...");
                            running = false;
                            break;
                        }
                    }
                } catch (IOException ignored) {}

                try {
                    // 1) Read the request using ForkJoinPool
                    Future<Request> readFuture = readPool.submit(() -> receiveRequest(in));

                    Request request;
                    try {
                        request = readFuture.get();
                    } catch (ExecutionException ee) {
                        Throwable cause = ee.getCause();
                        // разворачиваем цепочку причин
                        while (cause instanceof RuntimeException && cause.getCause() != null) {
                            cause = cause.getCause();
                        }

                        if (cause instanceof SocketTimeoutException) {
                            // нормальная ситуация: просто нет данных
                            Thread.sleep(50);
                            continue;
                        } else if (cause instanceof EOFException) {
                            logger.info("Клиент закрыл соединение");
                            break;
                        } else if (cause instanceof IOException) {
                            logger.warning("Ошибка при чтении запроса: " + cause.getMessage());
                            break;
                        } else {
                            logger.warning("Неожиданное исключение при чтении: " + cause);
                            break;
                        }
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }


                    if (request == null) {
                        continue;
                    }

                    // 2) Process the request on cached thread pool
                    processPool.submit(() -> {
                        try {
                            String result = getResultWithLock(collectionManager, request);
                            Answer answer = new Answer(result);

                            // 3) Send the answer using a new Thread as requested
                            new Thread(() -> {
                                try {
                                    sendAnswer(out, answer);
                                } catch (IOException e) {
                                    logger.warning("Ошибка при отправке ответа: " + e.getMessage());
                                }
                            }).start();

                        } catch (Exception e) {
                            logger.warning("Ошибка при обработке запроса: " + e.getMessage());
                        }
                    });

                } catch (CancellationException ce) {
                    logger.warning("Чтение было отменено: " + ce.getMessage());
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.warning("Ошибка при работе с клиентом: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {}
        }
    }

    // This wrapper decides whether to take a read or write lock around getResult.
    // Heuristic used:
    // - If command.cllOnly is true we conservatively assume it may modify the collection -> use write lock.
    // - If the received request carries an object (e.g. add/update) -> use write lock.
    // - Otherwise use read lock.
    static String getResultWithLock(CollectionManager collectionManager, Request received) {
        Command command = received.getReqCommand();
        if (command == null) {
            return null;
        }

        boolean needWrite = false;
        try {
            // heuristics
            if (command.cllOnly) needWrite = true;
            if (received.getObject() != null) needWrite = true;

            if (needWrite) {
                collectionLock.writeLock().lock();
                try {
                    return getResult(collectionManager, received);
                } finally {
                    collectionLock.writeLock().unlock();
                }
            } else {
                collectionLock.readLock().lock();
                try {
                    return getResult(collectionManager, received);
                } finally {
                    collectionLock.readLock().unlock();
                }
            }
        } catch (Exception e) {
            logger.warning("Ошибка при выполнении команды с блокировкой: " + e.getMessage());
            return "Ошибка при выполнении команды";
        }
    }

    // Original getResult is preserved but made private to be used inside locking wrapper
    static String getResult(CollectionManager collectionManager, Request received) {
        String[] in_args = received.getArgs();
        Command command = received.getReqCommand();
        String result = null;
        if (command != null) {
            int uid = 0;
            String userName = "";
            if(received.userData != null)
            {
                try {
                    ResultSet set = DbStuff.exeQuery("SELECT id FROM users WHERE name ='" + received.userData.getUserName() + "';");
                    if(set.next()) {
                        uid = set.getInt("id");
                        userName = received.userData.getUserName();
                    }
                }
                catch (SQLException e)
                {
                    System.out.println(e.getMessage());
                }
            }
            logger.info("Выполнение запроса... " + "(пользователь: " + userName + ")");
            if (command.isSingle) {
                result = command.execute(uid);
            } else if (command.cllOnly) {
                result = command.execute(collectionManager, uid);
            } else if (received.getObject() != null && received.getArgs().length == 1) {
                result = command.execute(collectionManager, (MusicBand) received.getObject(), uid);
            } else if (received.getObject() != null && received.getArgs().length > 1) {
                result = command.execute(collectionManager, in_args, received.getObject(), uid);
            } else {
                result = command.execute(collectionManager, in_args, uid);
            }
            logger.info("Запрос выполнен");
        }
        return result;
    }

    static Request receiveRequest(InputStream in) throws IOException, ClassNotFoundException {
        DataInputStream din = new DataInputStream(in);

        int len = din.readInt();
        if (len <= 0) {
            throw new IOException("Неверная длина пакета: " + len);
        }

        byte[] data = new byte[len];
        din.readFully(data);

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            Request request = (Request) ois.readObject();
            logger.info("Получен запрос: " + Arrays.toString(request.getArgs()));
            return request;
        }
    }


    static void sendAnswer(OutputStream out, Answer answer) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(answer);
        oos.flush();
        byte[] response = bos.toByteArray();

        out.write(ByteBuffer.allocate(4).putInt(response.length).array());
        out.write(response);
        out.flush();

        logger.info("Ответ отправлен клиенту");
    }

    static boolean checkUser(UserData userData)
    {
        String[] args = {"", userData.getUserName(), userData.getUserPassword()};
        if(new CheckUser().execute(collectionManager, args, 0).equals("y"));
        {
            return true;
        }
    }
}
