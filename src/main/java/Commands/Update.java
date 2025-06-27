package Commands;

import Classes.Command;
import Classes.MusicBand;
import Managers.CollectionManager;

import java.util.ArrayDeque;
import java.util.ArrayList;

/** Класс команды Update, наследуется от Command */
public class Update extends Command {

    /** Конструктор присваивает имя и описание */
    public Update()
    {
        name = "update";
        description = "обновить значение элемента коллекции, id которого равен заданному\n" +
                "выполнение:\n" +
                "update id_группы\n";
    }

    /** Даёт пользователю обновить значение элемента по id
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public String execute(CollectionManager collectionManager, String[] args, Object musB) {
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

            System.out.println("Обновить MusicBand:\nВведите название:");
            MusicBand mb = (MusicBand) musB;


            mbList.set(index, new MusicBand(mbID, mb.getName(), mb.getCoordinates(), mb.getPartsNum(), mb.getGenre(), mb.getLabel()));
            collectionManager.mbCollection = new ArrayDeque<>(mbList);
            return "Обновлено!";
        }
        catch (ArrayIndexOutOfBoundsException | NumberFormatException e)
        {
            return "Неверный id!";
        }
        }

}