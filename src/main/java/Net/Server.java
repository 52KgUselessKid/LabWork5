package Net;

import Classes.Command;
import Classes.MusicBand;
import Commands.Load;
import Commands.Save;
import Managers.CollectionManager;
import Managers.CommandManager;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        CollectionManager collectionManager = new CollectionManager();

        Load load = new Load(); load.execute(collectionManager, new String[]{null, "cll.xml"});

        // Регистрация Shutdown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Сервер отключен\n" + new Save().execute(collectionManager, new String[]{"", "cll.xml"}));
        }));

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Сервер запущен, ожидание подключения...");
while (true) {
    try (Socket clientSocket = serverSocket.accept();
         ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
         ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {

        System.out.println("Клиент подключен");

        while (true) {
            try {
                // Получаем объект от клиента
                Request received = (Request) ois.readObject();

//            if(received.isHeavy)
//            {
//                received = (HeRequest) ois.readObject();
//            }
                // Модифицируем объект и отправляем обратно

                Answer answer = new Answer(getResult(collectionManager, received));

                oos.writeObject(answer);
                oos.flush();
            }
            catch (EOFException e)
            {
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
        String[] in_args = received.getArgs();//scanner.nextLine().trim();

        Command command = received.getReqCommand();

        String result;
        if (command.isSingle) {
            result = command.execute();
        } else if (command.cllOnly) {
            result = command.execute(collectionManager);
        } else if (received.getObject() != null && received.getArgs() == null) {
            result = command.execute(collectionManager, (MusicBand) received.getObject());
        }
        else if (received.getObject() != null && received.getArgs() != null) {
            result = command.execute(collectionManager, in_args, received.getObject());
        }
        else {
            result = command.execute(collectionManager, in_args);
        }
        return result;
    }
    }