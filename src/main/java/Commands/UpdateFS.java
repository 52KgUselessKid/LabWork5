package Commands;

import Classes.Command;
import Classes.Coordinates;
import Classes.Label;
import Classes.MusicBand;
import Enums.MusicGenre;
import Managers.CollectionManager;

import java.util.ArrayDeque;
import java.util.ArrayList;

/** Класс команды Update, наследуется от Command */
public class UpdateFS extends Command {

    String xml;
    /** Конструктор присваивает имя и описание */
    public UpdateFS(String xml)
    {
        name = "updatefs";
        description = "обновить значение элемента коллекции, id которого равен заданному\n" +
                "выполнение:\n" +
                "update id_группы\n";
        this.xml = xml;
    }

    /** Даёт пользователю обновить значение элемента по id
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public String execute(CollectionManager collectionManager, String[] args) {
        try {
            int mbID = Integer.parseInt(args[1]);
            boolean go = false;
            for(MusicBand mb : collectionManager.mbCollection)
            {
                if(mb.getId() == mbID)
                {
                    go = true;
                    break;
                }
            }
            if(!go)
            {
                throw new ArrayIndexOutOfBoundsException();
            }
            ArrayList<MusicBand> mbList = new ArrayList<>(collectionManager.mbCollection);
            int index = 0;
            for (MusicBand musicBand : mbList) {
                if (musicBand.getId() == mbID) {
                    index = mbList.indexOf(musicBand);
                    break;
                }
            }

            String mbName = xml.split("<name>")[1].split("</name>")[0].trim();
            int mbX = Integer.parseInt(xml.split("<x>")[1].split("</x>")[0].trim());
            long mbY = Long.parseLong(xml.split("<y>")[1].split("</y>")[0].trim());
            long mbPartsNum = Long.parseLong(xml.split("<NumberOfParticipants>")[1].split("</NumberOfParticipants>")[0].trim());
            MusicGenre genre = MusicGenre.valueOf(xml.split("<genre>")[1].split("</genre>")[0].trim());
            Label mbLabel = new Label(xml.split("<label>")[1].split("</label>")[0].trim());

            mbList.set(index, new MusicBand(mbID, mbName, new Coordinates(mbX, mbY), mbPartsNum, genre, mbLabel));
            collectionManager.mbCollection = new ArrayDeque<>(mbList);
            return "Обновлено!";
        }
        catch (ArrayIndexOutOfBoundsException | NumberFormatException e)
        {
            return "Неверный id!";
        }
        }

}