package Commands;

import Classes.Command;
import DB.DbStuff;
import Managers.CollectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;

/** Класс команды Remove_head, наследуется от Command */
public class Remove_head extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public Remove_head()
    {
        name = "remove_head";
        description = "вывести первый элемент коллекции и удалить его\n";
        cllOnly = true;
        argCount = 1;
    }

    /** Даёт вывести первый элемент коллекции и удалить его
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public String execute(CollectionManager collectionManager, int uid) {
        int mbID = collectionManager.mbCollection.iterator().next().getId();

        System.out.println(mbID);
        System.out.println(uid);

        try {
            ResultSet set = DbStuff.exeQuery("SELECT userid FROM mbCollection WHERE id=" + mbID + ";");

            if (!set.next())
            {
                return "Нет группы с таким id!";
            }

            System.out.println(set.getInt("userid"));

            if (set.getInt("userid") == uid) {
                DbStuff.exeQueryVoid("DELETE FROM mbCollection WHERE id=" + mbID + ";");

                return collectionManager.mbCollection.stream()
                        .findFirst()
                        .map(musicBand -> {
                            System.out.println(musicBand);
                            collectionManager.mbCollection.removeFirst();
                            return "Первая группа удалена!";
                        })
                        .orElse("Коллекция пуста!");

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
        return "";
    }


}