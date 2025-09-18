package Commands;

import Classes.Command;
import DB.DbStuff;
import Managers.CollectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;

/** Класс команды Info, наследуется от Command */
public class CheckUser extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public CheckUser()
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
            ResultSet set = DbStuff.exeQuery("SELECT * FROM users WHERE name = '" + args[1] + "';");

            String password;

            if(args.length > 2)
            {
                password = args[2];
            }
            else
            {
                password = "";
            }
            if(!set.next()) {
                return "n";
            }
            if (DbStuff.toHash(password + set.getString("salt")).equals(set.getString("password"))) {
                return "y";
            }
            else
            {
                return "p";
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return "";
        }
    }

}