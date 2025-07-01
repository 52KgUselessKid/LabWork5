package Net;

import Classes.MusicBand;
import Managers.CollectionManager;

import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static Managers.CommandManager.input;

public class Client {
    static int maxRetryCount = 5;
    static int delay = 3;
    static int timeout = 3000;

    public static void main(String[] args) {
        openConsole();
    }

    public static void openConsole() {
        String input;
        int retryCount = 0;

        while (true) {
            try (SocketChannel channel = SocketChannel.open()) {

                channel.socket().connect(new InetSocketAddress("localhost", 12345), timeout);

                channel.configureBlocking(false);

                try (OutputStream out = Channels.newOutputStream(channel);
                     ObjectOutputStream oos = new ObjectOutputStream(out);
                     InputStream in = Channels.newInputStream(channel);
                     ObjectInputStream ois = new ObjectInputStream(in)) {

                    System.out.println("Вы подключились к серверу -_-");
                    retryCount = 0;

                    while (true) {
                        try {
                            input = input();
                            if (input == null || input.trim().isEmpty()) {
                                continue;
                            }

                            Request objToSend = getRequest(input);
                            if (objToSend.getReqCommand() == null) {
                                continue;
                            }

                            oos.writeObject(objToSend);
                            oos.flush();

                            Object response = ois.readObject();
                            if (response instanceof Answer) {
                                System.out.println(((Answer) response).getContent());
                            } else {
                                System.out.println("Неверный формат ответа от сервера");
                            }

                            if ("exit".equalsIgnoreCase(objToSend.getReqCommand().getName())) {
                                return;
                            }
                        } catch (SocketTimeoutException e) {
                            System.out.println("Таймаут операции");
                            break;
                        } catch (EOFException e) {
                            System.out.println("Сервер закрыл соединение");
                            break;
                        }
                    }
                }
            } catch (ConnectException | SocketTimeoutException e) {
                if (retryCount++ >= maxRetryCount) {
                    System.out.println("Сервер недоступен. Попробуйте позже.");
                    return;
                }
                System.out.printf("Попытка подключения %d/%d...\n", retryCount, maxRetryCount);
                try {
                    TimeUnit.SECONDS.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            } catch (Exception e) {
                System.out.println("Соединение прервано! Перерподключение...");
                if (retryCount++ >= maxRetryCount) {
                    return;
                }
                try {
                    TimeUnit.SECONDS.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    static Request getRequest(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String[] parts = input.split("\\s+");
        if (parts.length == 0) {
            return null;
        }

        String cName = parts[0].toLowerCase();
        try {
            if (cName.equals("add") || cName.equals("update")) {
                MusicBand mb = CollectionManager.getNewMB();
                return mb != null ? new Request(input, mb) : null;
            } else if (cName.equals("execute") && parts.length > 1) {
                String filePath = parts[1];
                if (!Files.exists(Paths.get(filePath))) {
                    System.out.println("Файл не найден: " + filePath);
                    return null;
                }
                String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
                return new Request(input, fileContent);
            }
            return new Request(input);
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка при создании запроса");
            return null;
        }
    }
}