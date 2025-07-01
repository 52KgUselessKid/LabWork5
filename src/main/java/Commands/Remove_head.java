package Commands;

import Classes.Command;
import Managers.CollectionManager;

/** Класс команды Remove_head, наследуется от Command */
public class Remove_head extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public Remove_head()
    {
        name = "remove_head";
        description = "вывести первый элемент коллекции и удалить его\n";
        cllOnly = true;
    }

    /** Даёт вывести первый элемент коллекции и удалить его
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public String execute(CollectionManager collectionManager) {
        return collectionManager.mbCollection.stream()
                .findFirst()
                .map(musicBand -> {
                    System.out.println(musicBand);
                    collectionManager.mbCollection.removeFirst();
                    return "Первая группа удалена!";
                })
                .orElse("Коллекция пуста!");}

}