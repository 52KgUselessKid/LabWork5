package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;
import Managers.CommandManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
        argCount = 2;
    }

    /** Позволяет пользователю выполнить скрипт
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public String execute(CollectionManager collectionManager, String[] args, Object object) {
        String output = "";
        try {
            String fileContent = (String) object;

            try (BufferedReader br = new BufferedReader(new StringReader(fileContent))) {

                String line;
                while ((line = br.readLine()) != null) {

                        String input = line.trim();
                        String[] args1 = input.strip().split("\\s+");

                        Command command;

                        if (input.strip().split("\\s+")[0].equals("update") || input.strip().split("\\s+")[0].equals("add")) {
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
                            if (input.strip().split("\\s+")[0].equals("update"))
                            {
                                command = new UpdateFS(xml);
                                output += command.execute(collectionManager, args1) + "\n";
                            }
                            else
                            {
                                command = new AddFS(xml);
                                output += command.execute(collectionManager) + "\n";
                            }
                        }
                        else {
                            if (cList.contains(input.strip().split("\\s+")[0])) {
                                command = CommandManager.getCommand(input.strip().split("\\s+")[0]);
                                if (command.isSingle) {
                                    output += command.execute();
                                } else if (command.cllOnly) {
                                    output += command.execute(collectionManager) + "\n";
                                } else {
                                    output += command.execute(collectionManager, args1) + "\n";
                                }
                            }
                        }
                }
            } catch (IOException e) {
                return "Нет такого файла!";
            }
        }
            catch (ArrayIndexOutOfBoundsException e)
            {
                return  "Введите путь к файлу!";
            }
    return output;
    }
}