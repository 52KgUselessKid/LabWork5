package Commands;

import Classes.Command;
import Managers.CollectionManager;

/** Класс команды Add, наследуется от Command */
public class Add extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly */
    public Add()
    {
        name = "add";
        description = "добавить новый элемент в коллекцию, \nвыполнение:\nadd -> ввод, и каждое значение" +
                " вводите построчно, координаты тоже\n";
        cllOnly = true;
    }

    /** Позволяет пользователю добавить музыкальную группу
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public void execute(CollectionManager collectionManager) {
        System.out.println("Создать новый MusicBand:\nВведите название:");
        collectionManager.mbCollection.add(CollectionManager.getNewMB());
        System.out.println("Коллекция добавлена!");
    }
}