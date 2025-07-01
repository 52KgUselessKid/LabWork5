package Net;

import Classes.Command;
import Classes.MusicBand;
import Commands.Load;
import Commands.Save;
import Managers.CollectionManager;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;

public class Server {
    public static Logger logger = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
        //setupLogger();

        logger.info("Инициализация сервера...");

        CollectionManager collectionManager = new CollectionManager();

        logger.info("Загрузка данных коллекции...");

        logger.info(new Load().execute(collectionManager, new String[]{null, "cll.xml"}));

        // Регистрация Shutdown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Сервер отключен\n" + new Save().execute(collectionManager, new String[]{"", "cll.xml"}));
        }));

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            logger.info("Сервер запущен, ожидание подключения...");
            while (true) {


                try (Socket clientSocket = serverSocket.accept();
                     ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                     ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {

                    logger.info("Клиент подключен!");

                    while (true) {
                        try {
                            // Получаем объект от клиента
                            Request received = (Request) ois.readObject();
                            logger.info("Получен запрос:" + Arrays.toString(received.getArgs()));

                            Answer answer = new Answer(getResult(collectionManager, received));

                            oos.writeObject(answer);
                            oos.flush();

                            logger.info("Ответ клиенту отправлен!");
                        }
                        catch (EOFException e)
                        {
                            logger.info("Клиент отключился о сервера 0_0");
                            break;
                        }
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String getResult(CollectionManager collectionManager, Request received)
    {
        logger.info("Выполнение запроса...");

        String[] in_args = received.getArgs();

        Command command = received.getReqCommand();

        String result;
        if (command.isSingle) {
            result = command.execute();
        } else if (command.cllOnly) {
            result = command.execute(collectionManager);
        } else if (received.getObject() != null && received.getArgs().length == 1) {
            result = command.execute(collectionManager, (MusicBand) received.getObject());
        }
        else if (received.getObject() != null && received.getArgs().length > 1) {
            result = command.execute(collectionManager, in_args, received.getObject());
        }
        else {
            result = command.execute(collectionManager, in_args);
        }
        logger.info("Запрос выполнен!");
        return result;
    }
}