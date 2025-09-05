package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.util.stream.Stream;

/** Класс команды Add, наследуется от Command */
public class Add extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly */
    public Add()
    {
        name = "add";
        description = "добавить новый элемент в коллекцию, \nвыполнение:\nadd -> ввод, и каждое значение" +
                " вводите построчно, координаты тоже\n";
        argCount = 1;
    }

    /** Позволяет пользователю добавить музыкальную группу
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public String execute(CollectionManager collectionManager, MusicBand mb) {
        int newMBandID = 1;
        for(MusicBand mBand : collectionManager.mbCollection)
        {
            if(mBand.getId() > newMBandID)
            {
                newMBandID = mBand.getId();
            }
        }
        newMBandID++;
        MusicBand mBand = new MusicBand(newMBandID, mb.getName(), mb.getCoordinates(), mb.getPartsNum(), mb.getGenre(), mb.getLabel());
        return Stream.ofNullable(mBand)
                .peek(band -> collectionManager.mbCollection.add(band))
                .findFirst()
                .map(band -> "В коллекцию добавлена группа!")
                .orElse("Не удалось добавить группу - объект равен null");
    }
}