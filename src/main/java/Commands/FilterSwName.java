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
    }

    /** Вывести элементы, значение поля name которых начинается с заданной подстроки
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public String execute(CollectionManager collectionManager, String[] args) {
        return collectionManager.mbCollection.stream() // Создаём Stream из ArrayDeque
                .filter(musicBand -> musicBand.getName() != null) // Проверяем, что имя не null
                .filter(musicBand -> musicBand.getName().startsWith(args[1])) // Фильтруем по началу строки
                .sorted(Comparator.comparing(MusicBand::getName))  // Сортировка по имени
                .map(MusicBand::toString) // Преобразуем в строку (или можно оставить сам объект)
                .collect(Collectors.joining()); // Объединяем в одну строку
    }
}