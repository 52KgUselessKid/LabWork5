package Commands;

import Classes.Command;

public class Exit extends Command {
    public Exit()
    {
        this.name = "exit";
        this.description = "завершить программу (без сохранения в файл)";
        isSingle = true;
    }
    @Override
    public void execute() {
        System.exit(0);
    }

}