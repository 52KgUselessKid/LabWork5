import Commands.Load;
import Managers.CollectionManager;
import Managers.CommandManager;

public class Main {
    public static void main(String[] args) {

        CollectionManager collectionManager = new CollectionManager();

        CommandManager commandManager = new CommandManager();

        Load load = new Load(); load.execute(collectionManager, new String[]{null, "cll.xml"});

        commandManager.openConsole(collectionManager);

    }
}