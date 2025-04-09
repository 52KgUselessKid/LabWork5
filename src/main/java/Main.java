import Classes.Coordinates;
import Classes.MusicBand;
import Enums.MusicGenre;
import Managers.CollectionManager;
import Managers.CommandManager;

public class Main {
    public static void main(String[] args) {

        CollectionManager collectionManager = new CollectionManager();

//        collectionManager.mbCollection.add(new MusicBand("Bombardiro", new Coordinates(3, 4), 12, MusicGenre.JAZZ, null));
//        collectionManager.mbCollection.add(new MusicBand("Tralallero", new Coordinates(3, 4), 2, MusicGenre.MATH_ROCK, null));
//        collectionManager.mbCollection.add(new MusicBand("Bombini", new Coordinates(3, 4), 6, MusicGenre.JAZZ, null));

        CommandManager commandManager = new CommandManager();

        commandManager.openConsole(collectionManager); //10.34

    }
}