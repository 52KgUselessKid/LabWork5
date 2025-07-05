package Net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 12345;


        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // блокирующий вызов
                System.out.println("Клиент подключен: " + clientSocket.getInetAddress());

                try (
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
                        BufferedWriter out = new BufferedWriter(
                                new OutputStreamWriter(clientSocket.getOutputStream()))
                ) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println("Получено: " + line);
                        out.write(line);
                        out.newLine();
                        out.flush();
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка при общении с клиентом: " + e.getMessage());
                }

                clientSocket.close();
                System.out.println("Клиент отключился.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}