package Commands;

import Classes.Command;
import Managers.CollectionManager;

/** Класс команды Info, наследуется от Command */
public class Info extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public Info()
    {
        name = "info";
        description = "вывести в стандартный поток вывода информацию о коллекции";
        cllOnly = true;
    }

    /** Даёт пользователю информацию о коллекции
     @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public void execute(CollectionManager collectionManager) {
        System.out.println(collectionManager.getCollectionInfo());
    }

}