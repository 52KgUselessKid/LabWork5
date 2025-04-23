package Classes;

/** Класс, представляющий лейбл */
public class Label {

    /** Имя лейбла */
    String name;

    /** Конструктор
     * @param name имя лейбла */
    public Label(String name) {
        this.name = name;
    }

    /** Создает объект Label из строки в формате XML
     * @param xml строка в формате XML
     * @return новый объект Label */
    public static Label fromXML(String xml) {
        String labelName = xml.split("<label>")[1].split("</label>")[0].trim();
        return new Label(labelName);
    }

    /** Возвращает строковое представление лейбла
     * @return имя лейбла */
    @Override
    public String toString() {
        return name;
    }
}