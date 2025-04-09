package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.util.Comparator;
import java.util.List;

public class Print_numOfParts extends Command {

    public Print_numOfParts()
    {
        this.name = "print_numparts";
        this.description = "вывести значения поля numberOfParticipants всех элементов в порядке убывания";
        cllOnly = true;
    }
    @Override
    public void execute(CollectionManager collectionManager) {

        List<Long> sortedParticipants = collectionManager.mbCollection.stream()
                .map(MusicBand::getPartsNum)
                .sorted(Comparator.reverseOrder())
                .toList();

        System.out.println(sortedParticipants);
    }

}
