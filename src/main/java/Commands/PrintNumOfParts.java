package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.util.Comparator;
import java.util.List;

/** Класс команды Print_numParts, наследуется от Command */
public class PrintNumOfParts extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public PrintNumOfParts()
    {
        name = "print";
        description = "вывести значения поля numberOfParticipants всех элементов в порядке убывания\n";
        cllOnly = true;
    }

    /** Позволяет пользователю вывести numberOfParticipants всех элементов в порядке убывания
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public String execute(CollectionManager collectionManager) {

        List<String> sortedParticipants = collectionManager.mbCollection.stream()
                .sorted(Comparator.comparing(MusicBand::getPartsNum).reversed())
                .map(band -> band.getName() + ": " + band.getPartsNum())
                .toList();

        return sortedParticipants.toString();
    }
}