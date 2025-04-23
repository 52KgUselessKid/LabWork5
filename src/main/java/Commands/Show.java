package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

/** Класс команды Show, наследуется от Command */
public class Show extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public Show()
    {
        name = "show";
        description = "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
        cllOnly = true;
    }

    /** Отображает пользователю все элементы коллекции
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public void execute(CollectionManager collectionManager) {
       for(MusicBand musicBand : collectionManager.mbCollection)
       {
           System.out.println(musicBand);
       }
    }


}