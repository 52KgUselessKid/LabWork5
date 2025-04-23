package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.util.Map;
import java.util.stream.Collectors;

/** Класс команды Group_count, наследуется от Command */
public class Group_count extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public Group_count()
    {
        name = "group_count";
        description = "сгруппировать элементы коллекции по значению поля genre, вывести количество элементов в каждой группе";
        cllOnly = true;
    }

    /** Сгруппировать элементы коллекции по значению поля genre, вывести количество элементов в каждой группе
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public void execute(CollectionManager collectionManager) {

        Map<String, Long> countByGenre = collectionManager.mbCollection.stream()
                .collect(Collectors.groupingBy(MusicBand::getGenreName, Collectors.counting()));


        countByGenre.forEach((genre, count) -> {
            System.out.println(genre + ": " + count);
        });
    }
}