package Commands;

import Classes.Command;
import Classes.MusicBand;
import DB.DbStuff;
import Managers.CollectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    public String execute(CollectionManager collectionManager, String[] args, Object musB, int uid) {
        try {
            int mbID = Integer.parseInt(args[1]);

            try {
                ResultSet set = DbStuff.exeQuery("SELECT userid FROM mbCollection WHERE id=" + mbID + ";");

                if (!set.next()) {
                    return "Нет группы с таким id!";
                }

                if (set.getInt("userid") == uid)
                {
                    MusicBand mBand = (MusicBand) musB;
                    DbStuff.exeQueryVoid("UPDATE mbCollection SET name ='" + mBand.getName() +"', coordinate_x =" + mBand.getCoordinates().getX() +
                            ", coordinate_y =" + mBand.getCoordinates().getY() + ", creationdate='" + mBand.getCDate() + "', " +
                            "numberofparticipants =" + mBand.getPartsNum() + ", genre ='" + mBand.getGenre() + "', label_ ='" + mBand.getLabel() + "' " +
                            "WHERE id =" + mbID + ";");
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
                                            mb.getPartsNum(), mb.getGenre(), mb.getLabel(), uid);
                                }
                                return musicBand;
                            })
                            .collect(Collectors.toList());

                    collectionManager.mbCollection = new ArrayDeque<>(updatedList);
                    return "Обновлено!";
                }
                else
                {
                    return "Эта группа вам не принадлежит!";
                }


            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }


        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return "Неверный id!";
        }
        return "";
    }


}