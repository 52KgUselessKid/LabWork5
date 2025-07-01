package Managers;

import Classes.Coordinates;
import Classes.Label;
import Classes.MusicBand;
import Enums.MusicGenre;

import java.time.LocalDateTime;
import java.util.ArrayDeque;

import static Managers.CommandManager.input;

/** Класс CollectionManger, содержит коллекциюи методы связанные с ней */
    public class CollectionManager {

    /** Коллекция муз групп */
    public ArrayDeque<MusicBand> mbCollection = new ArrayDeque<MusicBand>();

    /** Дата создания группы */
    final LocalDateTime dateCreated = LocalDateTime.now();

    /** Путь к группе */
    final String pathToCollection = System.getenv("FILE_NAME");

    /** Возвращает новый объект муз группы
     * @param args знаяения группы которые вводит пользователь
     * @return новая музыкаьлная группа */
    public static MusicBand getNewMB(String[] args) {
        String name = args[1];
        int x = Integer.parseInt(args[2]), y = Integer.parseInt(args[3]);
        int numOfPrtns = Integer.parseInt(args[4]);
        String genreName = args[5].toUpperCase();
        MusicGenre musicGenre = null;
        for (MusicGenre mGenre : MusicGenre.values()) {
            if (genreName.equals(mGenre.name())) {
                musicGenre = mGenre;
                break;
            }
        }
        String labelName = args[6];
        return new MusicBand(name, new Coordinates(x, y), numOfPrtns, musicGenre, new Label(labelName));
    }

    /** Возвращает новый объект муз группы, каждое значение пользователь вводит отдельно
     * @return новая музыкаьлная группа */
    public static MusicBand getNewMB() {
        System.out.println("Введите название:");
        String name;
        while (true) {
            name = input();
            if (name == null) {
                System.err.println("Имя должно быть непустым");
            } else {
                break;
            }
        }

        System.out.println("Введите координаты:");
        Coordinates coordinates;
        while (true) {
            try {
                System.out.println("Введите x:");
                int x = Integer.parseInt(input());
                System.out.println("Введите y:");
                int y = Integer.parseInt(input());
                coordinates = new Coordinates(x, y);
                break;
            } catch (NumberFormatException e) {
                System.err.println("Неверно введена координата, повторите ввод!");
            }
        }
        System.out.println("Введите кол-во участников:");
        int numOfPrtns = 0;
        while (numOfPrtns <= 0) {
            try {
                numOfPrtns = Integer.parseInt(input());
            } catch (NumberFormatException e) {
                System.err.println("неверно введено, введите число!");
            }
        }
        System.out.println("Введите жанр:");
        String genreName;
        MusicGenre musicGenre = null;
        while (true) {
            try {
                genreName = input().toUpperCase();
                for (MusicGenre mGenre : MusicGenre.values()) {
                    if (genreName.equals(mGenre.name())) {
                        musicGenre = mGenre;
                        break;
                    }
                }
                if (musicGenre != null) {
                    break;
                } else {
                    System.out.println("Такого жанра нет");
                    for(MusicGenre genre : MusicGenre.values())
                    {
                        System.out.print(genre + ", \n");
                    }
                }
            } catch (NullPointerException e) {
                System.out.println("Введите жанр жиесть");
            }
        }

        System.out.println("Введите лэйбл:");
        String labelName = input();
        return new MusicBand(name, coordinates, numOfPrtns, musicGenre, new Label(labelName));
    }

    /** Выдает информацию о группе
     * @return информацмя о группе*/
    public String getCollectionInfo() {
        String output = "Коллекция типа " + mbCollection.getClass().getSimpleName();
        output += " содержит " + mbCollection.size() + " групп. \n";
        output += "Дата создания: " + dateCreated + ".\n";
        output += "Коллекция хранится в " + pathToCollection + ".";

        return output;
    }
}
