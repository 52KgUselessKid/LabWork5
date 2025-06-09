package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/** Класс команды Load, наследуется от Command */
public class Load extends Command {

    /**
     * Конструктор присваивает имя и описание
     */
    public Load() {
        name = "load";
        description = "загрузить коллекцию из файла\n" +
                "выполнение:\n" +
                "load путь_к_файлу\n";
    }

    /**
     * Даёт пользователю загрузить коллекцию из файла
     *
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args              параметры для команды
     */
    @Override
    public void execute(CollectionManager collectionManager, String[] args) {

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(args[1]))) {
            byte[] buffer = new byte[bis.available()];
            bis.read(buffer);
            String xmlContent = new String(buffer);

            String[] xmlBands = xmlContent.split("<MusicBand>");
            for (int i = 1; i < xmlBands.length; i++) {
                if (!xmlBands[i].trim().isEmpty()) {
                    MusicBand musicBand = MusicBand.toMBand(xmlBands[i]);
                    collectionManager.mbCollection.add(musicBand);
                }
            }

            System.out.println("Загружено!");
        } catch (Exception e) {
            System.out.println("Нет такого файла!");}
        }
    }