package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.util.Comparator;
import java.util.stream.Collectors;

/** Класс команды Show, наследуется от Command */
public class Show extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public Show()
    {
        name = "show";
        description = "вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n";
        cllOnly = true;
        argCount = 1;
    }

    /** Отображает пользователю все элементы коллекции
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public String execute(CollectionManager collectionManager) {
        return collectionManager.mbCollection.stream()
                .sorted(Comparator.comparing(MusicBand::getName))  // Сортировка по имени
                .map(MusicBand::toString)
                .collect(Collectors.joining());  // Объединение в одну строку
}

}