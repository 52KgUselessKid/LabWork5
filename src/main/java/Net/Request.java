package Net;

import Classes.Command;

import java.io.Serializable;

import static Managers.CommandManager.getCommand;

public class Request implements Serializable {

    Command command;
    String[] args;
    Object input;
    //boolean isHeavy;

    public Request(String content)
    {
        args = content.split(" ");
        command = getCommand(args[0].strip());
    }

    public Request(String content, Object object)
    {
        args = content.split(" ");
        command = getCommand(args[0].strip());
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