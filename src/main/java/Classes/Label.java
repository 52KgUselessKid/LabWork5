package Classes;

public class Label {
    String name;

    public Label(String name) {
        this.name = name;
    }

    public static Label fromXML(String xml) {
        String labelName = xml.split("<label>")[1].split("</label>")[0].trim();
        return new Label(labelName);
    }

    @Override
    public String toString() {
        return name;
    }
}