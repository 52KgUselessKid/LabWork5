package Commands;

import Classes.Command;
import Managers.CommandManager;

public class Help extends Command {

    public  Help()
    {
        this.name = "help";
        this.description = "вывести справку по доступным командам";
        isSingle = true;
    }

    @Override
    public void execute() {
        String[] commands = new String[]{"help", "info", "show", "add", "addpro", "update", "updatepro","remove", "clear", "save", "execute", "exit", "head", "remove_head", "remove_lower", "group_count", "filter", "print_numparts"};         //"help, info" ahahah stupid nigger
        for(String commandName: commands){
            Command command = CommandManager.getCommand(commandName);
            System.out.println(command.getDescription());
        }
    }
}