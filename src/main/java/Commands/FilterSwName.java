package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

/** Класс команды Filter, наследуется от Command */
public class FilterSwName extends Command {

    /** Конструктор присваивает имя и описание*/
    public FilterSwName()
    {
        name = "filter";
        description = "вывести элементы, значение поля name которых начинается с заданной подстроки\n" +
                "выполнение:\n" +
                "filter подстрока\n";
    }

    /** Вывести элементы, значение поля name которых начинается с заданной подстроки
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public void execute(CollectionManager collectionManager, String[] args) {
        for(MusicBand musicBand : collectionManager.mbCollection)
        {
            if(musicBand.getName().startsWith(args[1]))
            {
                System.out.println(musicBand);
            }
        }
    }
}