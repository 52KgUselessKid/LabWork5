package Commands;

import Classes.Command;
import Classes.Coordinates;
import Classes.Label;
import Classes.MusicBand;
import Enums.MusicGenre;
import Managers.CollectionManager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class UpdatePro extends Command {


    public UpdatePro()
    {
        this.name = "updatepro";
        this.description = "обновить значение элемента коллекции, id которого равен заданному (Как нормальные люди)";
    }

    @Override
    public void execute(CollectionManager collectionManager, String[] args) {
        int mbID = Integer.parseInt(args[1]);
        ArrayList<MusicBand> mbList = new ArrayList<>(collectionManager.mbCollection);
        int index = 0;
        for(MusicBand musicBand : mbList)
        {
            if(musicBand.getId() == mbID)
            {
                index = mbList.indexOf(musicBand);
                break;
            }
        }
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));
        argList.remove(1);
        String[] fixedArgs = argList.toArray(new String[0]);
        String name = fixedArgs[1];
        int x = Integer.parseInt(fixedArgs[2]), y = Integer.parseInt(fixedArgs[3]);
        int numOfPrtns = Integer.parseInt(fixedArgs[4]);
        String genreName = fixedArgs[5].toUpperCase();
        MusicGenre musicGenre = null;
        for(MusicGenre mGenre : MusicGenre.values())
        {
            if(genreName.equals(mGenre.name()))
            {
                musicGenre = mGenre;
                break;
            }
        }
        String labelName = fixedArgs[6];
        mbList.get(index).setStats(name, new Coordinates(x, y), numOfPrtns, musicGenre, new Label(labelName));
        collectionManager.mbCollection = new ArrayDeque<>(mbList);
    }

}