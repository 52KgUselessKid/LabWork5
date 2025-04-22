package Commands;

import Classes.Command;
import Managers.CollectionManager;

public class Head extends Command {
    public Head()
    {
        name = "head";
        description = "вывести первый элемент коллекции";
        cllOnly = true;
    }

    @Override
    public void execute(CollectionManager collectionManager) {
        System.out.println(collectionManager.mbCollection.getFirst());
    }


}