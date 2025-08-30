package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.util.Comparator;
import java.util.stream.Collectors;

/** Класс команды Filter, наследуется от Command */
public class FilterSwName extends Command {

    /** Конструктор присваивает имя и описание*/
    public FilterSwName()
    {
        name = "filter";
        description = "вывести элементы, значение поля name которых начинается с заданной подстроки\n" +
                "выполнение:\n" +
                "filter подстрока\n";
        argCount = 2;
    }

    /** Вывести элементы, значение поля name которых начинается с заданной подстроки
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public String execute(CollectionManager collectionManager, String[] args) {
        return collectionManager.mbCollection.stream()
                .filter(musicBand -> musicBand.getName() != null)
                .filter(musicBand -> musicBand.getName().startsWith(args[1]))
                .sorted(Comparator.comparing(MusicBand::getName))
                .map(MusicBand::toString)
                .collect(Collectors.joining());
    }
}