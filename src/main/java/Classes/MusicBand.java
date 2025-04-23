package Classes;

import Enums.MusicGenre;

import java.util.Date;

/** Класс, представляющий музыкальную группу */
public class MusicBand implements Comparable<MusicBand> {

    /** счетчик уникального id */
    static int idCounter = 1;

    /** id группы */
    int id; // > 0

    /** Имя группы */
    String name; // not null

    /** координаты группы */
    Coordinates coordinates; // not null

    /** дата создания */
    Date creationDate; // not null

    /** кол-во участников */
    long numberOfParticipants; // > 0

    /** Жанр */
    MusicGenre genre; // not null

    /** Лейбл */
    Label label; // not null

    /** Конструктор */
    public MusicBand(String name, Coordinates coordinates, long numberOfParticipants, MusicGenre genre, Label label) {
        setStats(name, coordinates, numberOfParticipants, genre, label);
        this.id = idCounter++;
    }

    /** Конструктор */
    public MusicBand(int id, String name, Coordinates coordinates, long numberOfParticipants, MusicGenre genre, Label label) {
        setStats(name, coordinates, numberOfParticipants, genre, label);
        this.id = id;
        if (id >= idCounter) {
            idCounter = id + 1;
        }
    }

    /** Сеттер для полей */
    public void setStats(String name, Coordinates coordinates, long numberOfParticipants, MusicGenre genre, Label label) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("name != null or empty");
        if (coordinates == null) throw new IllegalArgumentException("coordinates != null");
        if (numberOfParticipants <= 0) throw new IllegalArgumentException("num of parts > 0");
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.numberOfParticipants = numberOfParticipants;
        this.genre = genre;
        this.label = label;
    }

    /** Возвращает id
     * @return id группы */
    public int getId() {
        return id;
    }

    /** Возвращает кол-во учасников
     * @return кол-во учасников */
    public long getPartsNum() {
        return numberOfParticipants;
    }

    /** Возвращает имя жанра группы
     * @return имя жанра группы */
    public String getGenreName() {
        return genre.name();
    }

    /** Возвращает жанр группы
     * @return жанр группы */
    public MusicGenre getGenre() {
        return genre;
    }

    /** Возвращает имя группы
     * @return имя группы */
    public String getName() {
        return name;
    }

    /** Возвращает координаты группы
     * @return координаты группы */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /** Возвращает имя лейбла группы
     * @return имя лейбла группы */
    public Label getLabel() {
        return label;
    }

    /** Возвращает Xml группы */
    public String getXML() {
        String xmlBlock = "";
        xmlBlock += "    <MusicBand>\n";
        xmlBlock += "        <id>" + id + "</id>\n";
        xmlBlock += "        <name>" + name + "</name>\n";
        xmlBlock += "        <coordinates>" + coordinates.x + "_" + coordinates.y + "</coordinates>\n";
        xmlBlock += "        <creationDate>" + creationDate.getTime() + "</creationDate>\n";
        xmlBlock += "        <NumberOfParticipants>" + numberOfParticipants + "</NumberOfParticipants>\n";
        xmlBlock += "        <genre>" + genre + "</genre>\n";
        xmlBlock += "        <label>" + label + "</label>\n";
        xmlBlock += "    </MusicBand>\n";
        return xmlBlock;
    }

    /** Возвращает новый объект группы из Xml
     * @param xml xml содержимое */
    public static MusicBand toMBand(String xml) {
        int id = Integer.parseInt(xml.split("<id>")[1].split("</id>")[0].trim());
        String name = xml.split("<name>")[1].split("</name>")[0].trim();
        Coordinates coordinates = Coordinates.fromXML(xml);
        long creationDateMillis = Long.parseLong(xml.split("<creationDate>")[1].split("</creationDate>")[0].trim());
        MusicGenre genre = MusicGenre.valueOf(xml.split("<genre>")[1].split("</genre>")[0].trim());
        Label label = Label.fromXML(xml);
        int numberOfParticipants = Integer.parseInt(xml.split("<NumberOfParticipants>")[1].split("</NumberOfParticipants>")[0].trim());
        MusicBand musicBand = new MusicBand(id, name, coordinates, numberOfParticipants, genre, label);
        musicBand.SetCDate(creationDateMillis);
        return musicBand;
    }

    /** Сеттер для даты создания группы
     * @param creationDateMillis значение даты в мс */
    public void SetCDate(long creationDateMillis) {
        creationDate = new Date(creationDateMillis);
    }

    /** Возвращает строковое представление группы
     * @return строковое представление группы */
    @Override
    public String toString() {
        return "MusicBand:" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", numberOfParticipants=" + numberOfParticipants +
                ", genre=" + genre +
                ", label=" + label +
                '}';
    }

    /** Сравнивает объекты групп между собой
     * @param other объект сравниваемой группы
     * @return положительное число если больше, отрицательное если меньше */
    @Override
    public int compareTo(MusicBand other) {
        return Long.compare(numberOfParticipants, other.getPartsNum());
    }
}