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
import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;

/** Класс команды Update, наследуется от Command */
public class UpdateFS extends Command {

    String xml;

    /**
     * Конструктор присваивает имя и описание
     */
    public UpdateFS(String xml) {
        name = "updatefs";
        description = "обновить значение элемента коллекции, id которого равен заданному\n" +
                "выполнение:\n" +
                "update id_группы\n";
        this.xml = xml;
        argCount = 2;
    }

    /**
     * Даёт пользователю обновить значение элемента по id
     *
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args              параметры для команды
     */
    @Override
    public String execute(CollectionManager collectionManager, String[] args, int uid) {
        try {
            int mbID = Integer.parseInt(args[1]);

            try {
                ResultSet set = DbStuff.exeQuery("SELECT userid FROM mbCollection WHERE id=" + mbID + ";");

                if (!set.next()) {
                    return "Нет группы с таким id!";
                }

                if (set.getInt("userid") == uid) {
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

                    MusicBand mBand = new MusicBand(mbID, mbName, new Coordinates(mbX, mbY), mbPartsNum, genre, mbLabel, uid);

                    DbStuff.exeQueryVoid("UPDATE mbCollection SET name ='" + mBand.getName() + "', coordinate_x =" + mBand.getCoordinates().getX() +
                            ", coordinate_y =" + mBand.getCoordinates().getY() + ", creationdate='" + mBand.getCDate() + "', " +
                            "numberofparticipants =" + mBand.getPartsNum() + ", genre ='" + mBand.getGenre() + "', label_ ='" + mBand.getLabel() + "' " +
                            "WHERE id =" + mbID + ";");


                    collectionManager.mbCollection = collectionManager.mbCollection.stream()
                            .map(mb -> mb.getId() == mbID
                                    ? new MusicBand(mbID, mbName, new Coordinates(mbX, mbY), mbPartsNum, genre, mbLabel, uid)
                                    : mb)
                            .collect(Collectors.toCollection(ArrayDeque::new));

                    return "Обновлено!";
                } else {
                    return "Эта группа вам не принадлежит!";
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return "";
    }
}