package Commands;

import Classes.Command;
import Managers.CollectionManager;

/** Класс команды Head, наследуется от Command */
public class Head extends Command {

    /** Конструктор присваивает имя, описание и переменную cllOnly*/
    public Head()
    {
        name = "head";
        description = "вывести первый элемент коллекции\n";
        cllOnly = true;
    }

    /** Даёт пользователю вывести первый элемент коллекции
     * @param collectionManager collectionManager содержащий коллекцию */
    @Override
    public String execute(CollectionManager collectionManager) {
//        return collectionManager.mbCollection.stream()
//                .findFirst() // Берёт первый элемент (Optional<MusicBand>)
//                .map(MusicBand::toString) // Преобразует в строку, если элемент есть
//                .orElse("Коллекция пуста!"); // Возвращает, если коллекция пуста

        return collectionManager.mbCollection.stream()
                .findFirst()
                .map(band -> {  // Лямбда-выражение вместо method reference
                    String bandInfo = band.toString();
                    return "Первый элемент коллекции: " + bandInfo;
                })
                .orElseGet(() -> {  // Лямбда для Supplier в orElseGet
                    System.out.println("Коллекция пуста, возвращаем сообщение");
                    return "Коллекция пуста!";
                });
    }


}