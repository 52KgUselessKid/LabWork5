package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.util.Comparator;
import java.util.List;

/** Класс команды Print_numParts, наследуется от Command */
public class Print_numOfParts extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public Print_numOfParts()
    {
        name = "print_numparts";
        description = "вывести значения поля numberOfParticipants всех элементов в порядке убывания";
        cllOnly = true;
    }

    /** Позволяет пользователю вывести numberOfParticipants всех элементов в порядке убывания
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public void execute(CollectionManager collectionManager) {

        List<Long> sortedParticipants = collectionManager.mbCollection.stream()
                .map(MusicBand::getPartsNum)
                .sorted(Comparator.reverseOrder())
                .toList();

        System.out.println(sortedParticipants);
    }
}