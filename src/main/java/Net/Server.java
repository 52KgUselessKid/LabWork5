package Net;

import Classes.Command;
import Classes.MusicBand;
import Commands.Load;
import Commands.Save;
import Exceptions.NotReceivedException;
import Managers.CollectionManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

public class Server {
    public static Logger logger = Logger.getLogger("logger");

    public static void main(String[] args) {
        logger.info("Инициализация сервера...");

        CollectionManager collectionManager = new CollectionManager();

        logger.info("Загрузка данных коллекции...");

        logger.info(new Load().execute(collectionManager, new String[]{null, "cll.xml"}));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Сервер отключен\n" /*+ new Save().execute(collectionManager, new String[]{null, "cll.xml"})*/);
        }));

        int port = 12345;
        boolean running = true;

        try (
                ServerSocket serverSocket = new ServerSocket(port);
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            logger.info("Сервер запущен, порт: " + port);

            while (running) {
                if (consoleReader.ready()) {
                    String line = consoleReader.readLine();
                    if ("save".equalsIgnoreCase(line.trim())) {
                        System.out.println(new Save().execute(collectionManager, new String[]{"", "cll.xml"}));
                        break;
                    }
                }

                Socket clientSocket = getClientSocket(serverSocket);

                try (
                        InputStream in = clientSocket.getInputStream();
                        OutputStream out = clientSocket.getOutputStream()
                ) {
                    while (running) {
                        if (consoleReader.ready()) {
                            String command = consoleReader.readLine();
                            if ("exit".equalsIgnoreCase(command.trim())) {
                                logger.info("Завершение работы сервера...");
                                running = false;
                                break;
                            }
                        }

                        try {
                            if (in.available() >= 4) {
                                Request request = receiveRequest(in);

                                Answer answer = new Answer(getResult(collectionManager, request));

                                sendAnswer(out, answer);
                            }
                        } catch (NotReceivedException | ClassNotFoundException e) {
                            System.out.println(e.getMessage());
                        }

                        Thread.sleep(50);
                    }
                } catch (IOException e) {
//                    e.printStackTrace();
                }

                clientSocket.close();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    static Socket getClientSocket(ServerSocket serverSocket) throws IOException {
        Socket clientSocket = serverSocket.accept();
        logger.info("Клиент подключен: " + clientSocket.getInetAddress());
        return clientSocket;
    }

    static Request receiveRequest(InputStream in) throws IOException, ClassNotFoundException {
            byte[] lenBytes = in.readNBytes(4);
            if (lenBytes.length < 4) throw new NotReceivedException();

            int length = ByteBuffer.wrap(lenBytes).getInt();
            byte[] data = in.readNBytes(length);
            if (data.length < length) throw new NotReceivedException();

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));

            Request request = (Request) ois.readObject();

            logger.info("Получен запрос: " + Arrays.toString(request.getArgs()));

            return request;
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