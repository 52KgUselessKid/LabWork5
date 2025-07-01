package Commands;

import Classes.Command;
import Managers.CollectionManager;

import java.util.ArrayDeque;
import java.util.stream.Collectors;

/** Класс команды Clear, наследуется от Command */
public class Clear extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly */
    public Clear()
    {
        name = "clear";
        description = "очистить коллекцию\n";
        cllOnly = true;
    }

    /** Позволяет очистить коллекцию
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public String execute(CollectionManager collectionManager) {
        collectionManager.mbCollection = collectionManager.mbCollection.stream()
                .filter(e -> false) // оставляет 0 элементов
                .collect(Collectors.toCollection(ArrayDeque::new));
        return "Коллекция очищена!";
    }
}