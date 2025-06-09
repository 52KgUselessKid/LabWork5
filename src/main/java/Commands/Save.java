package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/** Класс команды Save, наследуется от Command */
public class Save extends Command {

    /** Конструктор присваивает имя и описание */
    public Save()
    {
        name = "save";
        description = "сохранить коллекцию в файл\n" +
                "выполнение:\n" +
                "save путь_к_файлу\n";
    }

    /** Позволяет пользователю сохранить коллекцию в файл
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public void execute(CollectionManager collectionManager, String[] args) {
        try {
            String filename = args[1];
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<MusicBands>\n");

                for (MusicBand musicBand : collectionManager.mbCollection) {
                    writer.write(musicBand.getXML());
                }
                writer.write("</MusicBands>\n");
                System.out.println("Сохранено!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.out.println("Введите путь к файлу!");
        }
    }
}