package Commands;

import Classes.Command;
import Managers.CollectionManager;

public class AddPro extends Command {

    public AddPro()
    {
        name = "addpro";
        description = "добавить новый элемент в коллекцию (Как нормальные люди)";
    }

    @Override
    public void execute(CollectionManager collectionManager, String[] args) {
        collectionManager.mbCollection.add(CollectionManager.getNewMB(args));
        System.out.println("New MusicBand added to collection");
    }
}