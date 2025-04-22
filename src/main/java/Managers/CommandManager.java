package Managers;

import Classes.Command;
import Commands.*;
import Enums.Cmnds;
import Exceptions.InvalidCommandException;

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

    public static Command getCommand(String commandName){
        Command command = null;
        for(Cmnds cmnds : Cmnds.values())
        {
            if (commandName.toLowerCase().equals(cmnds.name()))
            {
                command = cmnds.getCommand();
                break;
            }
        }
        if(command == null) throw new InvalidCommandException();
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
