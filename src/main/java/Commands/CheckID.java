package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

/** Класс команды Info, наследуется от Command */
public class CheckID extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public CheckID()
    {
        name = "checkID";
        description = "вывести в стандартный поток вывода информацию о коллекции\n";
        argCount = 2;
    }

    /** Даёт пользователю информацию о коллекции
     @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public String execute(CollectionManager collectionManager, String[] args) {
        int mBandID = 0;
        try {
            mBandID = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e)
        {
            return "notOK";
        }
        for(MusicBand mBand : collectionManager.mbCollection)
        {
                if(mBandID == mBand.getId())
                {
                    return "ok";
                }
        }
        return "notOK";
    }

}