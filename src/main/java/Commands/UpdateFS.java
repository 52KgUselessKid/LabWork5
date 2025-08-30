package Commands;

import Classes.Command;
import Classes.Coordinates;
import Classes.Label;
import Classes.MusicBand;
import Enums.MusicGenre;
import Managers.CollectionManager;

import java.util.ArrayDeque;
import java.util.stream.Collectors;

/** Класс команды Update, наследуется от Command */
public class UpdateFS extends Command {

    String xml;
    /** Конструктор присваивает имя и описание */
    public UpdateFS(String xml)
    {
        name = "updatefs";
        description = "обновить значение элемента коллекции, id которого равен заданному\n" +
                "выполнение:\n" +
                "update id_группы\n";
        this.xml = xml;
        argCount = 2;
    }

    /** Даёт пользователю обновить значение элемента по id
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public String execute(CollectionManager collectionManager, String[] args) {
        try {
            int mbID = Integer.parseInt(args[1]);

            boolean exists = collectionManager.mbCollection.stream()
                    .anyMatch(mb -> mb.getId() == mbID);

            if (!exists) {
                throw new ArrayIndexOutOfBoundsException();
            }

            String mbName = xml.split("<name>")[1].split("</name>")[0].trim();
            int mbX = Integer.parseInt(xml.split("<x>")[1].split("</x>")[0].trim());
            long mbY = Long.parseLong(xml.split("<y>")[1].split("</y>")[0].trim());
            long mbPartsNum = Long.parseLong(xml.split("<NumberOfParticipants>")[1].split("</NumberOfParticipants>")[0].trim());
            MusicGenre genre = MusicGenre.valueOf(xml.split("<genre>")[1].split("</genre>")[0].trim());
            Label mbLabel = new Label(xml.split("<label>")[1].split("</label>")[0].trim());

            collectionManager.mbCollection = collectionManager.mbCollection.stream()
                    .map(mb -> mb.getId() == mbID
                            ? new MusicBand(mbID, mbName, new Coordinates(mbX, mbY), mbPartsNum, genre, mbLabel)
                            : mb)
                    .collect(Collectors.toCollection(ArrayDeque::new));

            return "Обновлено!";

        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return "Неверный id!";
        }
    }

}