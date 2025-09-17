package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;

/** Класс команды Update, наследуется от Command */
public class Update extends Command {

    /** Конструктор присваивает имя и описание */
    public Update()
    {
        name = "update";
        description = "обновить значение элемента коллекции, id которого равен заданному\n" +
                "выполнение:\n" +
                "update id_группы\n";
        argCount = 2;
    }

    /** Даёт пользователю обновить значение элемента по id
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public String execute(CollectionManager collectionManager, String[] args, Object musB) {
        try {
            int mbID = Integer.parseInt(args[1]);

            boolean exists = collectionManager.mbCollection.stream()
                    .anyMatch(mb -> mb.getId() == mbID);

            if (!exists) {
                throw new ArrayIndexOutOfBoundsException();
            }

            List<MusicBand> updatedList = collectionManager.mbCollection.stream()
                    .map(musicBand -> {
                        if (musicBand.getId() == mbID) {
                            MusicBand mb = (MusicBand) musB;
                            return new MusicBand(mbID, mb.getName(), mb.getCoordinates(),
                                    mb.getPartsNum(), mb.getGenre(), mb.getLabel(), 9);
                        }
                        return musicBand;
                    })
                    .collect(Collectors.toList());

            collectionManager.mbCollection = new ArrayDeque<>(updatedList);
            return "Обновлено!";

        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return "Неверный id!";
        }
    }


}