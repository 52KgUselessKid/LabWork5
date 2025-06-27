package Commands;

import Classes.Command;
import Managers.CollectionManager;

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
        collectionManager.mbCollection.clear();
        return "Коллекция очищена!";
    }
}