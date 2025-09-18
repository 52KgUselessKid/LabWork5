package Commands;

import Classes.Command;
import Classes.Coordinates;
import Classes.Label;
import Classes.MusicBand;
import DB.DbStuff;
import Enums.MusicGenre;
import Managers.CollectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

/** Класс команды Update, наследуется от Command */
public class AddFS extends Command {

    String xml;
    /** Конструктор присваивает имя и описание */
    public AddFS(String xml)
    {
        name = "addfs";
        description = "обновить значение элемента коллекции, id которого равен заданному\n" +
                "выполнение:\n" +
                "update id_группы\n";
        this.xml = xml;
        argCount = 2;
    }

    /** Даёт пользователю обновить значение элемента по id
     * @param collectionManager collectionManager содержащий коллекцию
     //* @param args параметры для команды */
    @Override
    public String execute(CollectionManager collectionManager, int uid) {
        int newMBandID = 1;
        try {
            try {
                String mbName = xml.split("<name>")[1].split("</name>")[0].trim();
                int mbX = Integer.parseInt(xml.split("<x>")[1].split("</x>")[0].trim());
                long mbY = Long.parseLong(xml.split("<y>")[1].split("</y>")[0].trim());
                long mbPartsNum = Long.parseLong(xml.split("<NumberOfParticipants>")[1].split("</NumberOfParticipants>")[0].trim());
                MusicGenre genre = MusicGenre.valueOf(xml.split("<genre>")[1].split("</genre>")[0].trim());
                Label mbLabel = new Label(xml.split("<label>")[1].split("</label>")[0].trim());

                MusicBand mb = new MusicBand(mbName, new Coordinates(mbX, mbY), mbPartsNum, genre, mbLabel, uid);

                DbStuff.exeQueryVoid("INSERT INTO mbCollection(name, coordinate_x, coordinate_y, creationdate, numberofparticipants, " +
                        "genre, label_, userid) VALUES('" + mb.getName() + "', " + mb.getCoordinates().getX() + ", " + mb.getCoordinates().getY() +
                        ", '" + mb.getCDate() + "', " + mb.getPartsNum() + ", '" + mb.getGenre() + "', '" + mb.getLabel() + "', " + mb.getUserID() + ");");

                ResultSet set = DbStuff.exeQuery("SELECT id FROM mbCollection WHERE name = '" + mb.getName() + "' AND userid = " + mb.getUserID() + ";");

                if(set.next())
                {
                    newMBandID = set.getInt("id");
                }

                MusicBand mBand = new MusicBand(newMBandID, mb.getName(), mb.getCoordinates(), mb.getPartsNum(), mb.getGenre(), mb.getLabel(), mb.getUserID());
                return Stream.ofNullable(mBand)
                        .peek(band -> collectionManager.mbCollection.add(band))
                        .findFirst()
                        .map(band -> "В коллекцию добавлена группа!")
                        .orElse("Не удалось добавить группу - объект равен null");

        } catch (ArrayIndexOutOfBoundsException | SQLException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
}