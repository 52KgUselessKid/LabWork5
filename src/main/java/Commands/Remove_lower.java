package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.util.Iterator;

/** Класс команды Remove_lower, наследуется от Command */
public class Remove_lower extends Command {

    /** Конструктор присваивает имя и описание */
    public Remove_lower()
    {
        name = "remove_lower";
        description = "удалить из коллекции все элементы, меньшие, чем заданный";
    }

    /** Удаляет из коллекции все элементы, меньшие, чем заданный
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
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