package Commands;

import Classes.Command;
import Managers.CollectionManager;

public class Clear extends Command {

    public Clear()
    {
        this.name = "clear";
        this.description = "очистить коллекцию";
        cllOnly = true;
    }
    @Override
    public void execute(CollectionManager collectionManager) {
        collectionManager.mbCollection.clear();
    }
}