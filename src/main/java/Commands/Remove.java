package Commands;

import Classes.Command;
import Managers.CollectionManager;

/** Класс команды Remove, наследуется от Command */
public class Remove extends Command {

    /** Конструктор присваивает имя и описание */
    public Remove()
    {
        name = "remove";
        description = "удалить элемент из коллекции по его id\n" +
                "выполнение:\n" +
                "remove id_группы\n";
    }

    /** Даёт удалить элемент из коллекции по его id
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public String execute(CollectionManager collectionManager, String[] args) {
        try {
            int mbID = Integer.parseInt(args[1]);

            // Используем Stream API для поиска и удаления элемента
            boolean removed = collectionManager.mbCollection.stream()
                    .filter(musicBand -> musicBand.getId() == mbID)
                    .findFirst()
                    .map(musicBand -> collectionManager.mbCollection.remove(musicBand))
                    .orElse(false);

            return removed ? "Группа удалена!" : "Нет группы с таким id!";
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return "Неверно введен id";
        }
    }

}