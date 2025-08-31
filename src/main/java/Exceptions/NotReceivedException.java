package Exceptions;

import java.io.IOException;

/** Класс исключения InvalidCommandException, наследуется от NullPointerException */
public class NotReceivedException extends IOException {

    /** Конструктор присваивает значение сообщению */
    public NotReceivedException()
    {
        super("Не получилось получить запрос :(");
    }
}