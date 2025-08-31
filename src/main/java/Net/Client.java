package Net;

import Classes.MusicBand;
import Managers.CollectionManager;

import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
    static Scanner scanner = new Scanner(System.in);
    static final int maxRetCount = 3;

    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int port = 12345;
        int retryCount = 0;
        String input = null;

        while (retryCount < maxRetCount) {
            try (
                    SocketChannel socketChannel = SocketChannel.open();
            ) {
                socketChannel.configureBlocking(false);
                socketChannel.connect(new InetSocketAddress(host, port));

                Selector selector = Selector.open();
                socketChannel.register(selector, SelectionKey.OP_CONNECT);

                ByteBuffer writeBuffer = ByteBuffer.allocate(8192);
                ByteBuffer readBuffer = ByteBuffer.allocate(8192);
                ByteArrayOutputStream readStream = new ByteArrayOutputStream();
                int expectedBytes = -1;

                while (true) {
                    selector.select(100); // неблокирующее ожидание

                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();

                        SocketChannel channel = (SocketChannel) key.channel();

                        if (key.isConnectable()) {
                            if (channel.finishConnect()) {
                                System.out.println("Вы подключились к серверу -_-");
                                channel.register(selector, SelectionKey.OP_WRITE);
                                retryCount = 0;
                            }
                        } else if (key.isWritable()) {
                            input = input();

                            Request request = getRequest(input);

                            if (input.split(" ").length != request.getReqCommand().getCommArgCount()) {
                                System.out.println("Неверное кол-во аргументов! (нужно " + (request.getReqCommand().getCommArgCount() - 1) + ")");
                                continue;
                            }

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                                oos.writeObject(request);
                            }

                            byte[] bytes = bos.toByteArray();

                            writeBuffer.clear();
                            writeBuffer.putInt(bytes.length);
                            writeBuffer.put(bytes);
                            writeBuffer.flip();

                            try {
                                while (writeBuffer.hasRemaining()) {
                                    channel.write(writeBuffer);
                                }
                            } catch (IOException e) {
                                System.out.println("Ошибка записи: " + e.getMessage());
                                key.cancel();
                                channel.close();
                                selector.close();
                                throw e;
                            }

                            channel.register(selector, SelectionKey.OP_READ);
                            readStream.reset();
                            expectedBytes = -1;
                        } else if (key.isReadable()) {
                            readBuffer.clear();
                            int read;
                            try {
                                read = channel.read(readBuffer);
                            } catch (IOException e) {
                                System.out.println("Ошибка чтения: " + e.getMessage());
                                key.cancel();
                                channel.close();
                                selector.close();
                                throw e;
                            }

                            if (read == -1) {
                                System.out.println("Сервер отключился.");
                                key.cancel();
                                channel.close();
                                selector.close();
                                throw new IOException("Сервер закрыл соединение");
                            }

                            readBuffer.flip();
                            while (readBuffer.hasRemaining()) {
                                readStream.write(readBuffer.get());
                            }

                            byte[] total = readStream.toByteArray();
                            if (expectedBytes == -1 && total.length >= 4) {
                                expectedBytes = ByteBuffer.wrap(total, 0, 4).getInt();
                            }

                            if (expectedBytes != -1 && total.length >= expectedBytes + 4) {
                                byte[] objectData = Arrays.copyOfRange(total, 4, 4 + expectedBytes);
                                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(objectData))) {
                                    Answer answer = (Answer) ois.readObject();
                                    if (answer.getContent() != null) {
                                        System.out.println(answer.getContent());
                                    }
                                } catch (ClassNotFoundException e) {
                                    System.out.println("Ошибка десериализации: " + e.getMessage());
                                }

                                channel.register(selector, SelectionKey.OP_WRITE);
                                readStream.reset();
                                expectedBytes = -1;
                            }

                            if ("exit".equalsIgnoreCase(input)) {
                                channel.close();
                                selector.close();
                                return;
                            }
                        }
                    }
                }

            } catch (ConnectException e) {
                retryCount++;
                System.out.println("Сервер недоступен. Попытка " + retryCount + " из " + maxRetCount);
                Thread.sleep(3000);
            } catch (IOException e) {
            }
        }

        System.out.println("Превышено число попыток подключения. Клиент завершает работу.");
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

    static String input() {
        System.out.print("$ ");
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            return "exit";
        }
    }
}