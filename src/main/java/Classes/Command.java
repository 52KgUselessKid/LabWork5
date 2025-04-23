package Classes;

import Interfaces.CCream;

/** Абстрактный класс, представляющий команду. Реализует интерфейс CCream */
public abstract class Command implements CCream {

    /** Имя команды */
    protected static String name;

    /** Описание команды */
    protected static String description;

    /** Указывает, является ли метод execute() без параметров */
    public boolean isSingle;

    /** Указывает, является ли метод execute() только с параметром коллекции */
    public boolean cllOnly;

    /** Возвращает имя команды
     * @return имя команды */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Возвращает описание команды
     * @return описание команды "имя -- описание" */
    @Override
    public String getDescription() {
        return name + " -- " + description;
    }
}