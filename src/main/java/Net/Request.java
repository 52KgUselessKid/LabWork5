package Net;

import Classes.Command;
import Exceptions.InvalidCommandException;

import java.io.Serializable;

import static Managers.CommandManager.getCommand;

public class Request implements Serializable {

    Command command;
    String[] args;
    Object input;

    public Request(String content)
    {
        args = content.strip().split("\\s+");
        try {
            command = getCommand(args[0].strip());
        }
        catch (InvalidCommandException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public Request(String content, Object object)
    {
        args = content.strip().split("\\s+");
        try {
            command = getCommand(args[0].strip());
        }
        catch (InvalidCommandException e)
        {
            System.out.println(e.getMessage());
        }
        input = object;
    }

    public Command getReqCommand()
    {
        return command;
    }

    public String[] getArgs()
    {
        return args;
    }

    Object getObject()
    {
        return input;
    }
}