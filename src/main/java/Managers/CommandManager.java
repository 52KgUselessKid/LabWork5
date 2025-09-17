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

                String[] args = input.strip().split("\\s+");

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
        Command command;

        switch (commandName.toLowerCase())
        {
            case "add" -> command = new Add();
            case "clear" -> command = new Clear();
            case "execute" -> command = new ExecuteScript();
            case "exit" -> command = new Exit();
            case "filter" -> command = new FilterSwName();
            case "group_count" -> command = new GroupCount();
            case "head" -> command = new Head();
            case "help" -> command = new Help();
            case "info" -> command = new Info();
            case "load" -> command = new Load();
            case "print" -> command = new PrintNumOfParts();
            case "remove" -> command = new Remove();
            case "remove_head" -> command = new Remove_head();
            case "remove_lower" -> command = new Remove_lower();
            case "save" -> command = new Save();
            case "show" -> command = new Show();
            case "update" -> command = new Update();
            //case "updatefs" -> command = new UpdateFS();
            case "check_id" -> command = new CheckID();
            case "auth" -> command = new CheckUser();
            case "add_user" -> command = new AddUser();
            case "get_user_id" -> command = new GetUserID();
            default -> throw new InvalidCommandException();
        }

        return command;
    }

    /** Модифицированный ввод, чтобы пустая строка распознавалась как null
     * @return ввод пользователя */
    public static String input()
    {
        System.out.print("$ ");
        String in = scanner.nextLine();
        if(in.isEmpty())
        {
            in = null;
        }
        return in;
    }
}
