package Commands;

import Classes.Command;
import Managers.CollectionManager;

import java.util.NoSuchElementException;

/** Класс команды Remove_head, наследуется от Command */
public class Remove_head extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public Remove_head()
    {
        name = "remove_head";
        description = "вывести первый элемент коллекции и удалить его\n";
        cllOnly = true;
    }

    /** Даёт вывести первый элемент коллекции и удалить его
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public void execute(CollectionManager collectionManager) {
        try {
            System.out.println(collectionManager.mbCollection.getFirst());
            collectionManager.mbCollection.removeFirst();
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Коллекция пуста!");
        }
    }

}