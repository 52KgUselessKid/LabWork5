package Commands;

import Classes.Command;
import Managers.CollectionManager;

public class Add extends Command {

    public Add()
    {
        name = "add";
        description = "добавить новый элемент в коллекцию";
        cllOnly = true;
    }

    @Override
    public void execute(CollectionManager collectionManager) {
        System.out.println("Создать новый MusicBand:\nВведите название:");
        collectionManager.mbCollection.add(CollectionManager.getNewMB());
        System.out.println("New MusicBand added to collection");
    }
}