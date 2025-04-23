package Commands;

import Classes.Command;
import Managers.CommandManager;

/** Класс команды Head, наследуется от Command */
public class Help extends Command {

    /** Конструктор присваивает имя, описание и переменную isSingle*/
    public Help() {
        name = "help";
        description = "вывести справку по доступным командам";
        isSingle = true;
    }

    /** Выдает пользователю спраку по командам*/
    @Override
    public void execute() {

        String[] cNames = {"add", "addpro", "clear", "execute", "exit", "filter", "group_count", "head", "help", "info",
        "load", "print_numparts", "remove", "remove_head", "remove_lower", "save", "show", "update", "updatepro"};


            for(String commandName : cNames)
            {
                Command command = CommandManager.getCommand(commandName);
                System.out.println(command.getDescription());
            }

        }
    }