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
        description = "удалить элемент из коллекции по его id\n" +
                "выполнение:\n" +
                "remove id_группы\n";
    }

    /** Даёт удалить элемент из коллекции по его id
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public String execute(CollectionManager collectionManager, String[] args) {
        try {
            int mbID = Integer.parseInt(args[1]);
            MusicBand mb = null;
            for (MusicBand musicBand : collectionManager.mbCollection) {
                if (musicBand.getId() == mbID) {
                    mb = musicBand;
                    collectionManager.mbCollection.remove(mb);
                    break;
                }
            }
            if(mb == null)
            {
                return "Нет группы с таким id!";
            }
        }
        catch (ArrayIndexOutOfBoundsException | NumberFormatException e)
        {
            return "Неверно введен id";
        }

        return "deleted";}

}