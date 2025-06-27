package Commands;

import Classes.Command;
import Managers.CollectionManager;

import java.util.NoSuchElementException;

/** Класс команды Head, наследуется от Command */
public class Head extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public Head()
    {
        name = "head";
        description = "вывести первый элемент коллекции\n";
        cllOnly = true;
    }

    /** Даёт пользователю вывести первый элемент коллекции
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public String execute(CollectionManager collectionManager) {
        try {
            return collectionManager.mbCollection.getFirst().toString();
        }
        catch (NoSuchElementException e)
        {
            return "Коллекция пуста!";
        }
    }


}