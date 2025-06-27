package Commands;

import Classes.Command;
import Managers.CollectionManager;

/** Класс команды AddPro, наследуется от Command */
public class AddPro extends Command {

    /** Конструктор присваивает имя и описание*/
    public AddPro()
    {
        name = "addpro";
        description = "добавить новый элемент в коллекцию (Как нормальные люди), \nвыполнение:\n" +
                "addpro имя x y кол-во_учатсников жанр лейбл\n";
    }

    /** Позволяет пользователю добавить музыкальную группу
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public String execute(CollectionManager collectionManager, String[] args) {
        collectionManager.mbCollection.add(CollectionManager.getNewMB(args));
        System.out.println("Коллекция добавлена!");
    return null;
    }
}