package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

public class Remove extends Command {

    public Remove()
    {
        name = "remove";
        description = "удалить элемент из коллекции по его id";
    }
    @Override
    public void execute(CollectionManager collectionManager, String[] args) {
        int mbID = Integer.parseInt(args[1]);
        for(MusicBand musicBand : collectionManager.mbCollection)
        {
            if(musicBand.getId() == mbID)
            {
                collectionManager.mbCollection.remove(musicBand);
                break;
            }
        }
    }

}