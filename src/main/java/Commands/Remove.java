package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

/** Класс команды Remove, наследуется от Command */
public class Remove extends Command {

    /** Конструктор присваивает имя и описание */
    public Remove()
    {
        name = "remove";
        description = "удалить элемент из коллекции по его id";
    }

    /** Даёт удалить элемент из коллекции по его id
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
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