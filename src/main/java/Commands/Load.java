package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** Класс команды Load, наследуется от Command */
public class Load extends Command {

    /** Конструктор присваивает имя и описание */
    public Load()
    {
        name = "load";
        description = "загрузить коллекцию из файла";
    }

    /** Даёт пользователю загрузить коллекцию из Xml файла
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
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
            for (int i = 1; i < xmlBands.length; i++) {
                if (!xmlBands[i].trim().isEmpty()) {
                    MusicBand musicBand = MusicBand.toMBand(xmlBands[i]);
                    collectionManager.mbCollection.add(musicBand);
                }
            }
    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
