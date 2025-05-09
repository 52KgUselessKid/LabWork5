package Commands;

import Classes.Command;
import Managers.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

/** Класс команды Execute, наследуется от Command */
public class Execute_script extends Command {

    /** Конструктор присваивает имя и описание */
    public Execute_script()
    {
        name = "execute";
        description = "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме";
    }

    /** Позволяет пользователю выполнить скрипт
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public void execute(CollectionManager collectionManager, String[] args) {

        String filename = args[1];

            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
                 BufferedReader br = new BufferedReader(new InputStreamReader(bis, StandardCharsets.UTF_8))) {

                String line;
                while ((line = br.readLine()) != null) {

                    String input = line.trim();
                    String[] args1 = input.split(" ");

                    Command command = CommandManager.getCommand(args1[0].strip());

                    if(command.isSingle) {
                        command.execute();
                    }
                    else if (command.cllOnly){
                        command.execute(collectionManager);
                    }
                    else
                    {
                        command.execute(collectionManager, args1);
                    }
                }

        } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
}