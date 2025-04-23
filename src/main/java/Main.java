import Managers.CollectionManager;
import Managers.CommandManager;

public class Main {
    public static void main(String[] args) {

        CollectionManager collectionManager = new CollectionManager();

        CommandManager commandManager = new CommandManager();

        commandManager.openConsole(collectionManager);

    }
}