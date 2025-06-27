package Net;

import Managers.CollectionManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import static Managers.CommandManager.input;

public class Client {

    public static void main(String[] args) {
    openConsole();
    }

    public static void openConsole()
    {


        String input;





        try (SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", 12345));
             // Создаем потоки ОДИН РАЗ при подключении
             OutputStream out = Channels.newOutputStream(channel);
             ObjectOutputStream oos = new ObjectOutputStream(out);
             InputStream in = Channels.newInputStream(channel);
             ObjectInputStream ois = new ObjectInputStream(in)) {
            while (true) {
                // Создаем тестовый объект для отправки
                input = input();
                if (input == null) input = "";

                Request objToSend = getRequest(input);

                oos.writeObject(objToSend);
                oos.flush();

                Answer answer = (Answer) ois.readObject();
                System.out.println(answer.getContent());

                if (objToSend.getReqCommand().getName().equals("exit")) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    static Request getRequest(String input)
    {
        String cName = input.split(" ")[0].strip().toLowerCase();

        if(cName.equals("add") || cName.equals("update"))
        {
            return new Request(input, CollectionManager.getNewMB());
        }  else if(cName.equals("execute")) {
            String filePath = input.split(" ")[1];
            File file = new File(filePath);

            if (!file.exists()) {
                System.out.println("Файл не найден: " + filePath);
                //continue;
            }

            // Читаем содержимое файла
            try {
                String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
                return new Request(input, fileContent);
            } catch (IOException e) {
                System.out.println("Ошибка чтения файла: " + e.getMessage());
                //continue;
            }
        }
        else{
            return new Request(input);}
    return null;
    }
}