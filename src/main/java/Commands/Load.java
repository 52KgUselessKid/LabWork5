package Commands;

import Classes.Command;
import Classes.Coordinates;
import Classes.Label;
import Classes.MusicBand;
import DB.DbStuff;
import Enums.MusicGenre;
import Managers.CollectionManager;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.stream.Stream;

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
        argCount = 2;
    }

    /**
     * Даёт пользователю загрузить коллекцию из файла
     *
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args              параметры для команды
     */
    @Override
    public String execute(CollectionManager collectionManager, String[] args) {

            new Clear().execute(collectionManager);

            try (ResultSet set = DbStuff.exeQuery("SELECT * FROM mbCollection;")) {
                while(set.next()){

                        MusicBand musicBand = new MusicBand(set.getInt("id"), set.getString("name"),
                                new Coordinates(set.getInt("coordinate_x"), set.getLong("coordinate_y")),
                                set.getLong("numberOfParticipants"), MusicGenre.valueOf(set.getString("genre")),
                                new Label(set.getString("label_")), set.getInt("userid"));
                        musicBand.setCDate(Long.parseLong(set.getString("creationDate")));
                        Stream.of(musicBand)
                                //.filter(Objects::nonNull)
                                .forEach(collectionManager.mbCollection::add);

                }

                return "Коллекция загружена!";
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
                return "Коллекция не загружена! Нет такого файла!";
            }
            }
    }
