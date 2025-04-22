package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Update extends Command {


    public Update()
    {
        name = "update";
        description = "обновить значение элемента коллекции, id которого равен заданному";
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

        System.out.println("Обновить MusicBand:\nВведите название:");
        MusicBand mb = CollectionManager.getNewMB();


        mbList.set(index, new MusicBand(mbID, mb.getName(), mb.getCoordinates(), mb.getPartsNum(), mb.getGenre(), mb.getLabel()));
        collectionManager.mbCollection = new ArrayDeque<>(mbList);
        System.out.println("MusicBand updated");
    }

}