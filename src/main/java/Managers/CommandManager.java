package Managers;

import Classes.Command;
import Commands.*;

import java.util.Scanner;

public class CommandManager {

    CollectionManager collectionManager;

    static Scanner scanner = new Scanner(System.in);

    public void openConsole(CollectionManager collectionManager)
    {
        this.collectionManager = collectionManager;
        String input;
        while (true) {
            try {
                input = input();
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
            catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static Command getCommand(String commandName){
        Command command;
        switch (commandName.toLowerCase()) {
            case "help" -> command = new Help();
            case "info" -> command = new Info();
            case "show" -> command = new Show();
            case "add" -> command = new Add();
            case "addpro" -> command = new AddPro();
            case "update" -> command = new Update();
            case "updatepro" -> command = new UpdatePro();
            case "remove" -> command = new Remove();
            case "clear" -> command = new Clear();
            case "save" -> command = new Save();
            case "load" -> command = new Load();
            case "execute" -> command = new Execute_script();
            case "exit" -> command = new Exit();
            case "head" -> command = new Head();
            case "remove_head" -> command = new Remove_head();
            case "remove_lower" -> command = new Remove_lower();
            case "group_count" -> command = new Group_count();
            case "filter" -> command = new Filter_sw_name();
            case "print_numparts" -> command = new Print_numOfParts();
            default -> command = null;
        }
        return command;
    }

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
