package Commands;

import Classes.Command;
import Managers.CommandManager;

/** Класс команды Head, наследуется от Command */
public class Help extends Command {

    /** Конструктор присваивает имя, описание и переменную isSingle*/
    public Help() {
        name = "help";
        description = "вывести справку по доступным командам\n";
        isSingle = true;
        argCount = 1;
    }

    /** Выдает пользователю спраку по командам*/
    @Override
    public String execute(int uid) {
        String output = "";

        String[] cNames = {"add", "clear", "execute", "exit", "filter", "group_count", "head", "help", "info",
                "load", "print", "remove", "remove_head", "remove_lower", "show", "update"};

        output += "***************************\n";
            for(String commandName : cNames)
            {
                Command command = CommandManager.getCommand(commandName);
                output += command.getDescription();
            }
        output += "***************************\n";
    return output;
    }
    }