package Exceptions;

public class InvalidCommandException extends NullPointerException {

    public InvalidCommandException()
    {
        super("No such a command or line is empty!");
    }
}