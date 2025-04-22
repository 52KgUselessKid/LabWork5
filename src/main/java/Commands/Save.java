package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Save extends Command {

    public Save()
    {
        name = "save";
        description = "сохранить коллекцию в файл";
    }

    @Override
    public void execute(CollectionManager collectionManager, String[] args) {
        String filename = args[1] + ".xml";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<MusicBands>\n");

                for (MusicBand musicBand : collectionManager.mbCollection) {
                   writer.write(musicBand.getXML());
                }
                writer.write("</MusicBands>\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }

}
