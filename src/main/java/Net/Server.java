package Net;

import Classes.Command;
import Classes.MusicBand;
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
import java.util.Arrays;
import java.util.logging.Logger;

public class Server {
    public static Logger logger = Logger.getLogger("logger");

    public static void main(String[] args) {
        logger.info("Инициализация сервера...");

        CollectionManager collectionManager = new CollectionManager();

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

        logger.info(new Load().execute(collectionManager, new String[]{null, cPath}));

        String path = cPath;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Сервер отключен\n" + new Save().execute(collectionManager, new String[]{null, path}));
        }));

        int port = 12345;
        boolean running = true;

        try (
                ServerSocket serverSocket = new ServerSocket(port);
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            logger.info("Сервер запущен, порт: " + port);

            while (running) {

                Socket clientSocket = getClientSocket(serverSocket);
                clientSocket.setSoTimeout(200);

                try (
                        InputStream in = clientSocket.getInputStream();
                        OutputStream out = clientSocket.getOutputStream()
                ) {
                    while (running && !clientSocket.isClosed()) {
                        if (consoleReader.ready()) {
                            String command = consoleReader.readLine();
                            if ("exit".equalsIgnoreCase(command.trim())) {
                                logger.info("Завершение работы сервера...");
                                running = false;
                                break;
                            }
                        }

                        try {
                            Request request;
                            try {
                                request = receiveRequest(in);
                            } catch (SocketTimeoutException ste) {
                                Thread.sleep(50);
                                continue;
                            }

                            Answer answer = new Answer(getResult(collectionManager, request));
                            sendAnswer(out, answer);

                        } catch (EOFException eof) {
                            logger.info("Клиент закрыл соединение");
                            break;
                        } catch (IOException ioe) {
                            logger.info("Ошибка связи с клиентом: " + ioe.getMessage());
                            break;
                        } catch (ClassNotFoundException cnf) {
                            logger.warning("Ошибка десериализации: " + cnf.getMessage());
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                } catch (IOException e) {
                    logger.warning("Ошибка при работе с клиентом: " + e.getMessage());
                } finally {
                    try {
                        clientSocket.close();
                    } catch (IOException ignored) {}
                }

            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    static Socket getClientSocket(ServerSocket serverSocket) throws IOException {
        Socket clientSocket = serverSocket.accept();
        logger.info("Клиент подключен: " + clientSocket.getInetAddress());
        return clientSocket;
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


    static String getResult(CollectionManager collectionManager, Request received) {
        String[] in_args = received.getArgs();
        Command command = received.getReqCommand();
        String result = null;
        if (command != null) {
            logger.info("Выполнение запроса...");
            if (command.isSingle) {
                result = command.execute();
            } else if (command.cllOnly) {
                result = command.execute(collectionManager);
            } else if (received.getObject() != null && received.getArgs().length == 1) {
                result = command.execute(collectionManager, (MusicBand) received.getObject());
            } else if (received.getObject() != null && received.getArgs().length > 1) {
                result = command.execute(collectionManager, in_args, received.getObject());
            } else {
                result = command.execute(collectionManager, in_args);
            }
            logger.info("Запрос выполнен");
        }
        return result;
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
}