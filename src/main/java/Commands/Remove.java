package Commands;

import Classes.Command;
import DB.DbStuff;
import Managers.CollectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;

/** Класс команды Remove, наследуется от Command */
public class Remove extends Command {

    /** Конструктор присваивает имя и описание */
    public Remove()
    {
        name = "remove";
        description = "удалить элемент из коллекции по его id\n" +
                "выполнение:\n" +
                "remove id_группы\n";
        argCount = 2;
    }

    /** Даёт удалить элемент из коллекции по его id
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public String execute(CollectionManager collectionManager, String[] args, int uid) {
        try {
            int mbID = Integer.parseInt(args[1]);
            try {
                ResultSet set = DbStuff.exeQuery("SELECT userid FROM mbCollection WHERE id=" + mbID + ";");

                if (!set.next())
                {
                    return "Нет группы с таким id!";
                }

                    if (set.getInt("userid") == uid) {
                        DbStuff.exeQueryVoid("DELETE FROM mbCollection WHERE id=" + mbID + ";");
                        boolean removed = collectionManager.mbCollection.stream()
                                .filter(musicBand -> musicBand.getId() == mbID)
                                .findFirst()
                                .map(musicBand -> collectionManager.mbCollection.remove(musicBand))
                                .orElse(false);

                        return removed ? "Группа удалена!" : "Нет группы с таким id!";
                    }
                    else
                    {
                        return "Эта группа вам не принадлежит!";
                    }

            }
            catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }

        catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return "Неверно введен id";
        }
        return "";
    }

}