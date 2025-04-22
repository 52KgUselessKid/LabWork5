package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

public class Filter_sw_name extends Command {

    public Filter_sw_name()
    {
        name = "filter";
        description = "вывести элементы, значение поля name которых начинается с заданной подстроки";
    }
    @Override
    public void execute(CollectionManager collectionManager, String[] args) {
        for(MusicBand musicBand : collectionManager.mbCollection)
        {
            if(musicBand.getName().startsWith(args[1]))
            {
                System.out.println(musicBand);
            }
        }
    }
}