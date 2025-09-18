package Commands;

import Classes.Command;
import Classes.MusicBand;
import DB.DbStuff;
import Managers.CollectionManager;

import java.sql.SQLException;

/** Класс команды Info, наследуется от Command */
public class AddUser extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public AddUser()
    {
        name = "adduser";
        description = "вывести в стандартный поток вывода информацию о коллекции\n";
        argCount = 2;
    }

    /** Даёт пользователю информацию о коллекции
     @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public String execute(CollectionManager collectionManager, String[] args, int uid) {
        try {
            String password;

            if(args.length > 2)
            {
                password = args[2];
            }
            else
            {
                password = "";
            }

            String salt = DbStuff.getSalt();
            String hashedPassword = DbStuff.toHash(password + salt);
            DbStuff.exeQueryVoid("INSERT INTO users (name, password, salt) VALUES ('" + args[1] + "', '" + hashedPassword + "', " +
                    "'" + salt + "');");
            System.out.println("jbjj");
            return "Профиль создан! Можете входить в систему!";
        }
        catch (SQLException e)
        {
            System.out.println("kkmkm");
            return e.getMessage();
        }
    }

}