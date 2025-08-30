package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.util.Optional;

/** Класс команды Remove_lower, наследуется от Command */
public class Remove_lower extends Command {

    /** Конструктор присваивает имя и описание */
    public Remove_lower()
    {
        name = "remove_lower";
        description = "удалить из коллекции все элементы, меньшие, чем заданный\n" +
                "выполнение:\n" +
                "remove_lower id_группы\n";
        argCount = 2;
    }

    /** Удаляет из коллекции все элементы, меньшие, чем заданный
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public String execute(CollectionManager collectionManager, String[] args) {
        try {
            int mbID = Integer.parseInt(args[1]);

            Optional<MusicBand> currentMBOpt = collectionManager.mbCollection.stream()
                    .filter(musicBand -> musicBand.getId() == mbID)
                    .findFirst();

            if (!currentMBOpt.isPresent()) {
                return "Нет группы с таким id";
            }

            MusicBand currentMB = currentMBOpt.get();

            collectionManager.mbCollection.removeIf(musicBand ->
                    currentMB.compareTo(musicBand) > 0);

            return "Удалено!";

        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return "Неверно введён id";
        }}
}