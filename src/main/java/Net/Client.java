package Net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 12345;

        try (
                SocketChannel socketChannel = SocketChannel.open();
                Scanner scanner = new Scanner(System.in)
        ) {
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(host, port));

            while (!socketChannel.finishConnect()) {
                // Подключение
            }

            System.out.println("Подключено к серверу. Введите строку:");

            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) break;

                writeBuffer.clear();
                writeBuffer.put((input + "\n").getBytes());
                writeBuffer.flip();
                while (writeBuffer.hasRemaining()) {
                    socketChannel.write(writeBuffer);
                }

                // Чтение ответа
                readBuffer.clear();
                int bytesRead;
                while ((bytesRead = socketChannel.read(readBuffer)) == 0) {
                    // подождать немного или использовать Selector
                }

                if (bytesRead == -1) break;

                readBuffer.flip();
                byte[] data = new byte[readBuffer.remaining()];
                readBuffer.get(data);
                System.out.println("Сервер ответил: " + new String(data).trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}