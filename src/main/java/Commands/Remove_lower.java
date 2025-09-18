package Commands;

import Classes.Command;
import Classes.MusicBand;
import DB.DbStuff;
import Managers.CollectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** Класс команды Remove_lower, наследуется от Command */
public class Remove_lower extends Command {

    /** Конструктор присваивает имя и описание */
    public Remove_lower()
    {
        name = "remove_lower";
        description = "удалить из коллекции все элементы, меньшие, чем заданный\n" +
                "выполнение:\n" +
                "remove_lower id_группы\n";
        argCount = 2;
    }

    /** Удаляет из коллекции все элементы, меньшие, чем заданный
     * @param collectionManager collectionManager содержащий коллекцию
     * @param args параметры для команды */
    @Override
    public String execute(CollectionManager collectionManager, String[] args, int uid) {
        try {
            int mbID = Integer.parseInt(args[1]);

            // Найдём опорную группу в коллекции (нужно, чтобы иметь объект для compareTo)
            Optional<MusicBand> currentMBOpt = collectionManager.mbCollection.stream()
                    .filter(musicBand -> musicBand.getId() == mbID)
                    .findFirst();

            if (!currentMBOpt.isPresent()) {
                return "Нет группы с таким id!";
            }

            MusicBand currentMB = currentMBOpt.get();

            // Проверяем владельца опорной группы в БД и одновременно убеждаемся, что запись существует
            try (ResultSet set = DbStuff.exeQuery("SELECT userid FROM mbCollection WHERE id=" + mbID + ";")) {
                if (set == null || !set.next()) {
                    return "Нет группы с таким id!";
                }

                int ownerId = set.getInt("userid");
                if (ownerId != uid) {
                    return "Эта группа вам не принадлежит!";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return "Ошибка при обращении к базе данных: " + e.getMessage();
            }

            // === 1) Удаляем из базы данных элементы пользователя uid, которые "меньше" опорной группы ===
            // ВАЖНО: здесь используется условие id < mbID — это предположение, что compareTo фактически совпадает с сравнением по id.
            // Если compareTo использует другое поле (например, name, participants и т.п.), SQL нужно изменить соответствующим образом.
            try {
                String deleteSql = "DELETE FROM mbCollection WHERE userid = " + uid + " AND id < " + mbID + ";";
                DbStuff.exeQueryVoid(deleteSql);
            } catch (Exception e) {
                // DbStuff.exeQueryVoid может кидать SQLException или RuntimeException — поймаем и вернём сообщение
                e.printStackTrace();
                return "Ошибка при удалении из базы данных: " + e.getMessage();
            }

            // === 2) Синхронизируем коллекцию в памяти ===
            int before = collectionManager.mbCollection.size();
            collectionManager.mbCollection.removeIf(musicBand ->
                    musicBand.getUserID() == uid && currentMB.compareTo(musicBand) > 0
            );
            int after = collectionManager.mbCollection.size();
            int removedCount = before - after;

            if (removedCount > 0) {
                return "Удалено " + removedCount + " элементов пользователя с id = " + uid +
                        ", которые меньше чем заданная группа!";
            } else {
                return "Ничего не удалено (нет подходящих элементов)";
            }

        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return "Неверно введён id!";
        }
    }
}