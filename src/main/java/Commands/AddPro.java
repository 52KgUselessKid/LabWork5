package Commands;

import Classes.Command;
import Managers.CollectionManager;

/** Класс команды AddPro, наследуется от Command */
public class AddPro extends Command {

    /** Конструктор присваивает имя и описание*/
    public AddPro()
    {
        name = "addpro";
        description = "добавить новый элемент в коллекцию (Как нормальные люди)";
    }

    /** Позволяет пользователю добавить музыкальную группу
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public void execute(CollectionManager collectionManager, String[] args) {
        collectionManager.mbCollection.add(CollectionManager.getNewMB(args));
        System.out.println("New MusicBand added to collection");
    }
}