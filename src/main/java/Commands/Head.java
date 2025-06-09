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
    public void execute(CollectionManager collectionManager) {
        try {
            System.out.println(collectionManager.mbCollection.getFirst());
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Коллекция пуста!");
        }
    }


}