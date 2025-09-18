package Commands;

import Classes.Command;
import DB.DbStuff;
import Managers.CollectionManager;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.stream.Collectors;

/** Класс команды Clear, наследуется от Command */
public class Clear extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly */
    public Clear()
    {
        name = "clear";
        description = "очистить коллекцию\n";
        cllOnly = true;
        argCount = 1;
    }

    /** Позволяет очистить коллекцию
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public String execute(CollectionManager collectionManager, int uid) {
        try {
            DbStuff.exeQueryVoid("DELETE FROM mbCollection WHERE userid =" + uid + ";");
            collectionManager.mbCollection = collectionManager.mbCollection.stream()
                    .filter(e -> e.getUserID() != uid)
                    .collect(Collectors.toCollection(ArrayDeque::new));
            return "Коллекция очищена!";
        }
        catch (SQLException e)
        {
            return e.getMessage();
        }
    }
}