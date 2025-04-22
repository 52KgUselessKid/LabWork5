package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

public class Show extends Command {

    public Show()
    {
        name = "show";
        description = "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
        cllOnly = true;
    }

    @Override
    public void execute(CollectionManager collectionManager) {
       for(MusicBand musicBand : collectionManager.mbCollection)
       {
           System.out.println(musicBand);
       }
    }


}