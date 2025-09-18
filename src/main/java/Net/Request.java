package Net;

import Classes.Command;
import Exceptions.InvalidCommandException;

import java.io.Serializable;

import static Managers.CommandManager.getCommand;

public class Request implements Serializable {

    Command command;
    String[] args;
    Object input;
    UserData userData;

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

    public Request(String content, UserData userData)
    {
        args = content.strip().split("\\s+");
        try {
            command = getCommand(args[0].strip());
        }
        catch (InvalidCommandException e)
        {
            System.out.println(e.getMessage());
        }
        this.userData = userData;
    }

    public Request(String content, Object object, UserData userData)
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
        this.userData = userData;
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