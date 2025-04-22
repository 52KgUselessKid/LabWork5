package Commands;

import Classes.Command;
import Managers.CollectionManager;

public class Clear extends Command {

    public Clear()
    {
        name = "clear";
        description = "очистить коллекцию";
        cllOnly = true;
    }
    @Override
    public void execute(CollectionManager collectionManager) {
        collectionManager.mbCollection.clear();
    }
}