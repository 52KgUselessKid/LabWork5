package Net;

import Classes.Command;
import Classes.MusicBand;
import Commands.Save;
import Managers.CollectionManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

public class Server {
    public static Logger logger = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
        CollectionManager collectionManager = new CollectionManager();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Сервер отключен\n" + new Save().execute(collectionManager, new String[]{"", "cll.xml"}));
        }));

        int port = 12345;
        boolean running = true;

        try (
                ServerSocket serverSocket = new ServerSocket(port);
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Сервер запущен на порту " + port);

            while (running) {
                // Проверка пользовательского ввода (команда exit)
                if (consoleReader.ready()) {
                    String line = consoleReader.readLine();
                    if ("save".equalsIgnoreCase(line.trim())) {
                        System.out.println(new Save().execute(collectionManager, new String[]{"", "cll.xml"}));
                        break;
                    }
                }

                // Проверка наличия клиента (неблокирующая альтернатива serverSocket.accept() отсутствует,
                // поэтому блокировка возможна. Чтобы обойти — можно использовать serverSocket.setSoTimeout, но это усложняет код.
                // Мы сохраняем ваш подход — клиент подключается по одному.
                Socket clientSocket = serverSocket.accept();
                System.out.println("Клиент подключен: " + clientSocket.getInetAddress());

                try (
                        InputStream in = clientSocket.getInputStream();
                        OutputStream out = clientSocket.getOutputStream()
                ) {
                    while (running) {
                        // Проверка ввода с клавиатуры внутри клиентского цикла
                        if (consoleReader.ready()) {
                            String command = consoleReader.readLine();
                            if ("exit".equalsIgnoreCase(command.trim())) {
                                System.out.println("Завершение работы сервера...");
                                running = false;
                                break;
                            }
                        }

                        if (in.available() >= 4) {
                            byte[] lenBytes = in.readNBytes(4);
                            if (lenBytes.length < 4) break;

                            int length = ByteBuffer.wrap(lenBytes).getInt();
                            byte[] data = in.readNBytes(length);
                            if (data.length < length) break;

                            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                            Request request = (Request) ois.readObject();
                            System.out.println("Получено: " + Arrays.toString(request.getArgs()));

                            Answer answer = new Answer(getResult(collectionManager, request));
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(bos);
                            oos.writeObject(answer);
                            oos.flush();
                            byte[] response = bos.toByteArray();

                            out.write(ByteBuffer.allocate(4).putInt(response.length).array());
                            out.write(response);
                            out.flush();
                        }

                        Thread.sleep(50); // Чтобы не грузить CPU
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Клиент отключился.");
                }

                clientSocket.close();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Сервер завершён.");
    }

    static String getResult(CollectionManager collectionManager, Request received) {
        String[] in_args = received.getArgs();
        Command command = received.getReqCommand();
        String result = null;
        if(command != null) {
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
        }
        return result;
    }
}
