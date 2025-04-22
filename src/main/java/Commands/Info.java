package Commands;

import Classes.Command;
import Managers.CollectionManager;

public class Info extends Command {

    public Info()
    {
        name = "info";
        description = "вывести в стандартный поток вывода информацию о коллекции";
        cllOnly = true;
    }
    @Override
    public void execute(CollectionManager collectionManager) {
        System.out.println(collectionManager.getCollectionInfo());
    }

}