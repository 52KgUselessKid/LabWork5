package Exceptions;

/** Класс исключения InvalidCommandException, наследуется от NullPointerException */
public class InvalidCommandException extends NullPointerException {

    /** Конструктор присваивает значение сообщению */
    public InvalidCommandException()
    {
        super("No such a command or line is empty!");
    }
}