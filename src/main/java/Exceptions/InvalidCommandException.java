package Exceptions;

/** Класс исключения InvalidCommandException, наследуется от NullPointerException */
public class InvalidCommandException extends NullPointerException {

    /** Конструктор присваивает значение сообщению */
    public InvalidCommandException()
    {
        super("Нет такой команды или строка пуста!");
    }
}