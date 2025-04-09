package Classes;

public class Coordinates {
    Integer x; // not null
    long y; // max 40

    public Coordinates(Integer x, long y) {
        if (x == null) throw new IllegalArgumentException("x != null");
        if (y > 40) throw new IllegalArgumentException("y <= 40");
        this.x = x;
        this.y = y;
    }

    public static Coordinates fromXML(String xml) {
        return new Coordinates(Integer.parseInt(String.valueOf(xml.split("_")[0])), Long.parseLong(String.valueOf(xml.split("_")[1])));
    }
}