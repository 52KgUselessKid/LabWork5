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
        return Stream.ofNullable(mb)
                .peek(band -> collectionManager.mbCollection.add(band))
                .findFirst()
                .map(band -> "В коллекцию добавлена группа!")
                .orElse("Не удалось добавить группу - объект равен null");
    }
}