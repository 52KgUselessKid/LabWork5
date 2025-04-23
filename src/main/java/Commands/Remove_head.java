package Commands;

import Classes.Command;
import Managers.CollectionManager;

/** Класс команды Remove_head, наследуется от Command */
public class Remove_head extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public Remove_head()
    {
        name = "remove_head";
        description = "вывести первый элемент коллекции и удалить его";
        cllOnly = true;
    }

    /** Даёт вывести первый элемент коллекции и удалить его
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public void execute(CollectionManager collectionManager) {
        System.out.println(collectionManager.mbCollection.getFirst());
        collectionManager.mbCollection.removeFirst();
    }

}