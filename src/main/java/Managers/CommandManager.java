package Managers;

import Classes.Command;
import Commands.*;
import Exceptions.InvalidCommandException;

import java.util.Scanner;

/** Класс CommandManager, содержит ввод пользователя и получение новой команды*/
public class CommandManager {

    /** CollectionManager который содержит коллекцию */
    CollectionManager collectionManager;

    /** Сканер для ввода */
    static Scanner scanner = new Scanner(System.in);

    /** С этого метода программа начинает принимать ввод от пользователя
     * @param collectionManager CollectionManager который содержит коллекцию */
    public void openConsole(CollectionManager collectionManager)
    {
        this.collectionManager = collectionManager;
        String input;
        while (true) {
            try {
                input = input();
                if (input == null) input = "";

                String[] args = input.split(" ");//scanner.nextLine().trim();
                Command command = getCommand(args[0].strip());

                if(command.isSingle) {
                    command.execute();
                }
                else if (command.cllOnly){
                    command.execute(collectionManager);
                }
                else
                {
                    command.execute(collectionManager, args);
                }
            }
            catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /** Возвращает новую команду
     * @param commandName имя команды которое ввел пользователь
     * @return новая команда*/
    public static Command getCommand(String commandName){
        Command command = null;

        switch (commandName.toLowerCase())
        {
            case "add" -> command = new Add();
            case "addpro" -> command = new AddPro();
            case "clear" -> command = new Clear();
            case "execute" -> command = new Execute_script();
            case "exit" -> command = new Exit();
            case "filter" -> command = new Filter_sw_name();
            case "group_count" -> command = new Group_count();
            case "head" -> command = new Head();
            case "help" -> command = new Help();
            case "info" -> command = new Info();
            case "load" -> command = new Load();
            case "print_numparts" -> command = new Print_numOfParts();
            case "remove" -> command = new Remove();
            case "remove_head" -> command = new Remove_head();
            case "remove_lower" -> command = new Remove_lower();
            case "save" -> command = new Save();
            case "show" -> command = new Show();
            case "update" -> command = new Update();
            case "updatepro" -> command = new UpdatePro();
            default -> throw new InvalidCommandException();
        }

        return command;
    }

    /** Модифицированный ввод, чтобы пустая строка распознавалась как null
     * @return ввод пользователя */
    public static String input()
    {
        String in = scanner.nextLine();
        if(in.isEmpty())
        {
            in = null;
        }
        return in;
    }
}
