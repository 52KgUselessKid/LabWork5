package Commands;

import Classes.Command;
import Managers.CollectionManager;

public class Remove_head extends Command {

    public Remove_head()
    {
        this.name = "remove_head";
        this.description = "вывести первый элемент коллекции и удалить его";
        cllOnly = true;
    }
    @Override
    public void execute(CollectionManager collectionManager) {
        System.out.println(collectionManager.mbCollection.getFirst());
        collectionManager.mbCollection.removeFirst();
    }

}