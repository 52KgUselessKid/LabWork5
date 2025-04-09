package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.util.Iterator;

public class Remove_lower extends Command {


    public Remove_lower()
    {
        this.name = "remove_lower";
        this.description = "удалить из коллекции все элементы, меньшие, чем заданный";
    }

    @Override
    public void execute(CollectionManager collectionManager, String[] args) {
        int mbID = Integer.parseInt(args[1]);
        MusicBand currentMB = null;
        for(MusicBand musicBand : collectionManager.mbCollection)
        {
            if(musicBand.getId() == mbID)
            {
                currentMB = musicBand;
                Iterator<MusicBand> iterator = collectionManager.mbCollection.iterator();
                while (iterator.hasNext()) {
                    MusicBand musicBand1 = iterator.next();
                    if (currentMB.compareTo(musicBand1) > 0) {
                        iterator.remove();
                    }
                }
                break;
            }
        }

    }
}
