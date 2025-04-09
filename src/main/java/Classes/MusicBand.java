package Classes;

import Enums.MusicGenre;

import java.util.Date;

public class MusicBand implements Comparable<MusicBand> {
    static int idCounter = 1;
    int id; // > 0
    String name; // not null
    Coordinates coordinates; // not null
    Date creationDate; // not null
    long numberOfParticipants; // > 0
    MusicGenre genre; // not null
    Label label; // not null

    public MusicBand(String name, Coordinates coordinates, long numberOfParticipants, MusicGenre genre, Label label) {
        setStats(name, coordinates, numberOfParticipants, genre, label);
        this.id = idCounter++;
    }

    public MusicBand(int id, String name, Coordinates coordinates, long numberOfParticipants, MusicGenre genre, Label label) {
        setStats(name, coordinates, numberOfParticipants, genre, label);
        this.id = id;
        if(id >= idCounter)
        {
            idCounter = id + 1;
        }
    }

    public void setStats(String name, Coordinates coordinates, long numberOfParticipants, MusicGenre genre, Label label)
    {
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

    public int getId()
    {
        return id;
    }

    public long getPartsNum()
    {
        return numberOfParticipants;
    }

    public String getGenreName()
    {
        return genre.toString();
    }

    public MusicGenre getGenre()
    {
        return genre;
    }

    public String getName()
    {
        return name;
    }
    public Coordinates getCoordinates()
    {
        return coordinates;
    }
    public Label getLabel()
    {
        return label;
    }

    public String getXML()
    {
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

    public static MusicBand toMBand(String xml)
    {
        int id = Integer.parseInt(xml.split("<id>")[1].split("</id>")[0].trim());
        String name = xml.split("<name>")[1].split("</name>")[0].trim();
        //System.out.println(name);
        Coordinates coordinates = Coordinates.fromXML(xml.split("<coordinates>")[1].split("</coordinates>")[0].trim());
        long creationDateMillis = Long.parseLong(xml.split("<creationDate>")[1].split("</creationDate>")[0].trim());
        MusicGenre genre = MusicGenre.valueOf(xml.split("<genre>")[1].split("</genre>")[0].trim());
        Label label = Label.fromXML(xml);
        int numberOfParticipants = Integer.parseInt(xml.split("<NumberOfParticipants>")[1].split("</NumberOfParticipants>")[0].trim());
        MusicBand musicBand = new MusicBand(id, name, coordinates, numberOfParticipants, genre, label);
        musicBand.SetCDate(creationDateMillis);
        return musicBand;
    }


    public void SetCDate(long creationDateMillis)
    {
        creationDate = new Date(creationDateMillis);
    }


    @Override
    public String toString() {
        return "MusicBand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", numberOfParticipants=" + numberOfParticipants +
                ", genre=" + genre +
                ", label=" + label +
                '}';
    }

    // Getters and setters, hashCode, equals, toString methods ...
    @Override
    public int compareTo(MusicBand other) {
        return Long.compare(numberOfParticipants, other.getPartsNum());
    }
}