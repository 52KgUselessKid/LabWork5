package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/** Класс команды Group_count, наследуется от Command */
public class GroupCount extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public GroupCount()
    {
        name = "group_count";
        description = "сгруппировать элементы коллекции по значению поля genre, вывести количество элементов в каждой группе\n";
        cllOnly = true;
    }

    /** Сгруппировать элементы коллекции по значению поля genre, вывести количество элементов в каждой группе
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public String execute(CollectionManager collectionManager) {
        ArrayList<String> output = new ArrayList<>();
        Map<String, Long> countByGenre = collectionManager.mbCollection.stream()
                .collect(Collectors.groupingBy(MusicBand::getGenreName, Collectors.counting()));


        countByGenre.forEach((genre, count) -> {
            output.add(genre + ": " + count);
        });
        return output.toString();
    }
}