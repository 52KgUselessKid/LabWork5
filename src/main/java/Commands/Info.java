package Commands;

import Classes.Command;
import Managers.CollectionManager;

/** Класс команды Info, наследуется от Command */
public class Info extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public Info()
    {
        name = "info";
        description = "вывести в стандартный поток вывода информацию о коллекции\n";
        cllOnly = true;
        argCount = 1;
    }

    /** Даёт пользователю информацию о коллекции
     @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public String execute(CollectionManager collectionManager, int uid) {
        return collectionManager.getCollectionInfo();
    }

}