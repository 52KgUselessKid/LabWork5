package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Load extends Command {

    public Load()
    {
        name = "load";
        description = "загрузить коллекцию из файла";
    }
    @Override
    public void execute(CollectionManager collectionManager, String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader(args[1]))) {
            String line;
            StringBuilder xmlBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                xmlBuilder.append(line).append("\n");
            }
            String xmlContent = xmlBuilder.toString();
            String[] xmlBands = xmlContent.split("<MusicBand>");
            //System.out.println(xmlBands[xmlBands.length-1]);
            for (int i = 1; i < xmlBands.length; i++) {
                if (!xmlBands[i].trim().isEmpty()) {
                    //bandXML += "</MusicBand>"; // Добавляем закрывающий тег
                    //System.out.println(bandXML);
                    MusicBand musicBand = MusicBand.toMBand(xmlBands[i]);
                    collectionManager.mbCollection.add(musicBand);
                }
            }
    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
