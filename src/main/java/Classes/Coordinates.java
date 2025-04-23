package Classes;

/** Класс, представляющий координаты */
public class Coordinates {

    /** Координата x */
    Integer x; // not null

    /** Координата y */
    long y; // max 40

    /** Конструктор
     * @param x значение координаты x
     * @param y значение координаты y
     * @throws IllegalArgumentException если x == null или y > 40 */
    public Coordinates(Integer x, long y) {
        if (x == null) throw new IllegalArgumentException("x != null");
        if (y > 40) throw new IllegalArgumentException("y <= 40");
        this.x = x;
        this.y = y;
    }

    /** Создает объект Coordinates из строки в формате XML.
     * @param xml строка в формате XML, содержащая значения x и y
     * @return новый объект Coordinates (x, y) */
    public static Coordinates fromXML(String xml) {
        String inXml = xml.split("<coordinates>")[1].split("</coordinates>")[0].trim();
        return new Coordinates(Integer.parseInt(inXml.split("_")[0]), Long.parseLong(inXml.split("_")[1]));
    }
}