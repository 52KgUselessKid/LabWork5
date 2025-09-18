package Commands;

import Classes.Command;
import DB.DbStuff;
import Managers.CollectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;

/** Класс команды Info, наследуется от Command */
public class GetUserID extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public GetUserID()
    {
        name = "check_user";
        description = "вывести в стандартный поток вывода информацию о коллекции\n";
        argCount = 2;
    }

    /** Даёт пользователю информацию о коллекции
     @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public String execute(CollectionManager collectionManager, String[] args, int uid) {
        try {
            ResultSet set = DbStuff.exeQuery("SELECT id FROM users WHERE name = '" + args[1] + "';");

            if(set.next()) {
                return Integer.toString(set.getInt("id"));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return "";
        }
        return "";
    }

}