package Commands;

import Classes.Command;
import Managers.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** Класс команды Execute, наследуется от Command */
public class ExecuteScript extends Command {

    String[] cNames = {"add", "addpro", "clear", "execute", "exit", "filter", "group_count", "head", "help", "info",
            "load", "print_numparts", "remove", "remove_head", "remove_lower", "save", "show", "update", "updatepro"};
    ArrayList<String> cList = new ArrayList<>(List.of(cNames));

    /** Конструктор присваивает имя и описание */
    public ExecuteScript()
    {
        name = "execute";
        description = "считать и исполнить скрипт из указанного файла\nвыполнение:\n" +
                "execute путь_к_скрипту\n";
    }

    /** Позволяет пользователю выполнить скрипт
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public void execute(CollectionManager collectionManager, String[] args) {

        try {
            String filename = args[1];

            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
                 BufferedReader br = new BufferedReader(new InputStreamReader(bis, StandardCharsets.UTF_8))) {

                String line;
                while ((line = br.readLine()) != null) {

                        String input = line.trim();
                        String[] args1 = input.split(" ");

                        Command command = null;

                        if (input.split(" ")[0].equals("update")) {
                            String xml;
                            StringBuilder xmlBuilder = new StringBuilder();
                            while ((line = br.readLine()) != null) {
                                line = line.trim();
                                if (line.equals("</MusicBand>")) {
                                    xmlBuilder.append(line);
                                    break;
                                }
                                if (!xmlBuilder.isEmpty()) {
                                    xmlBuilder.append("\n");
                                }
                                xmlBuilder.append(line);
                            }
                            xml = xmlBuilder.toString();
                            command = new UpdateFS(xml);
                            command.execute(collectionManager, args1);
                        }
                        else {
                            if (cList.contains(input)) {
                                command = CommandManager.getCommand(input);
                                if (command.isSingle) {
                                    command.execute();
                                } else if (command.cllOnly) {
                                    command.execute(collectionManager);
                                } else {
                                    command.execute(collectionManager, args1);
                                }
                            }
                        }
                }
            } catch (IOException e) {
                System.out.println("Нет такого файла!");
            }
        }
            catch (ArrayIndexOutOfBoundsException e)
            {
                System.out.println("Введите путь к файлу!");
            }
    }
}