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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Client {
    static Scanner scanner = new Scanner(System.in);
    static final int maxRetCount = 10;

    static SocketChannel socketChannel;
    static Selector selector;

    static ByteBuffer writeBuffer = ByteBuffer.allocate(8192);
    static ByteBuffer readBuffer = ByteBuffer.allocate(8192);
    static ByteArrayOutputStream readStream = new ByteArrayOutputStream();
    static int expectedBytes = -1;

    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int port = 12345;
        int retryCount = 0;
        String input;

        while (retryCount < maxRetCount) {
            try {
                connect(host, port);
                System.out.println("Вы подключились к серверу -_-");
                retryCount = 0;

                while (true) {
                    input = input();
                    Request request = getRequest(input);
                    if (request == null || request.getReqCommand() == null || !validateRequest(input, request)) {
                        continue;
                    }

                    sendRequest(request);
                    Answer answer = receiveAnswer();
                    if (answer != null && answer.getContent() != null) {
                        System.out.println(answer.getContent());
                    }

                    if ("exit".equalsIgnoreCase(input)) {
                        close();
                        return;
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

    static void connect(String host, int port) throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(host, port));

        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while (true) {
            selector.select(100);
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                SocketChannel channel = (SocketChannel) key.channel();
                if (key.isConnectable() && channel.finishConnect()) {
                    channel.register(selector, SelectionKey.OP_WRITE);
                    return;
                }
            }
        }
    }

    static void sendRequest(Request request) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(request);
        }
        byte[] bytes = bos.toByteArray();

        writeBuffer.clear();
        writeBuffer.putInt(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();

        while (writeBuffer.hasRemaining()) {
            socketChannel.write(writeBuffer);
        }

        socketChannel.register(selector, SelectionKey.OP_READ);
        readStream.reset();
        expectedBytes = -1;
    }

    static Answer receiveAnswer() throws IOException {
        while (true) {
            selector.select(100);
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();

                if (key.isReadable()) {
                    readBuffer.clear();
                    int read = socketChannel.read(readBuffer);
                    if (read == -1) {
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
                            return (Answer) ois.readObject();
                        } catch (ClassNotFoundException e) {
                            System.out.println("Ошибка десериализации: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    static void close() throws IOException {
        if (socketChannel != null) socketChannel.close();
        if (selector != null) selector.close();
    }

    static Request getRequest(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String[] parts = input.strip().split("\\s+");
        if (parts.length == 0) {
            return null;
        }

        String cName = parts[0].toLowerCase().trim();
        try {
            if (cName.equals("add") || cName.equals("update")) {
                Request tempReq = new Request(input);
                if (validateRequest(input, tempReq)) {
                    if (cName.equals("update"))
                    {
                        sendRequest(new Request("check_id " + parts[1]));
                        Answer answer = receiveAnswer();
                        if(!answer.getContent().equals("ok"))
                        {
                            System.out.println("Неверный id!");
                            return null;
                        }
                    }
                    MusicBand mb = CollectionManager.getNewMB();
                    return mb != null ? new Request(input, mb) : null;
                } else {
                    return null;
                }
            } else if (cName.equals("execute") && parts.length > 1) {
                String filePath = parts[1];
                Map<String, String> scripts = new LinkedHashMap<>();
                Set<String> visited = new LinkedHashSet<>();

                loadFileRecursive(filePath, scripts, visited);
                //System.out.println("nnnnnnnnnnnnnn\n" + scripts + "\nnnnnnnn");
                // Теперь в scripts лежит карта (имя файла → содержимое)
                return new Request(input, scripts);
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

    static void loadFileRecursive(String filePath, Map<String, String> scripts, Set<String> visited) throws IOException {
        if (visited.contains(filePath)) {
            return; // чтобы не зациклиться
        }
        visited.add(filePath);

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            System.out.println("Файл не найден: " + filePath);
            return;
        }

        String content = new String(Files.readAllBytes(path));
        scripts.put(filePath, content);

        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("execute ")) {
                String[] parts = line.split("\\s+", 2);
                if (parts.length > 1) {
                    String nestedFile = parts[1];
                    loadFileRecursive(nestedFile, scripts, visited);
                }
            }
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

    static boolean validateRequest(String input, Request request) {
        if (input.strip().split("\\s+").length != request.getReqCommand().getCommArgCount()) {
            System.out.println("Неверное кол-во аргументов! (нужно " + (request.getReqCommand().getCommArgCount() - 1) + ")");
            return false;
        }
        return true;
    }
}
