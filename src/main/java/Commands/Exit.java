package Commands;

import Classes.Command;

/** Класс команды Exit, наследуется от Command */
public class Exit extends Command {

    /** Конструктор присваивает имя, описание и переменную isSingle */
    public Exit()
    {
        name = "exit";
        description = "завершить программу (без сохранения в файл)";
        isSingle = true;
    }

    /** Позволяет пользователю завершить программу */
    @Override
    public void execute() {
        System.exit(0);
    }

}