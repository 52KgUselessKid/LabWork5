package Commands;

import Classes.Command;
import Enums.Cmnds;
import Exceptions.InvalidCommandException;
import Managers.CommandManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Help extends Command {

    public Help() {
        name = "help";
        description = "вывести справку по доступным командам";
        isSingle = true;
    }

    @Override
    public void execute() {

        String[] mm = new String[]{"help", "add"};


            System.out.println(CommandManager.getCommand("help").getName());


        }
    }