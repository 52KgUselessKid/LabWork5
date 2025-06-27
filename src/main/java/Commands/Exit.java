package Commands;

import Classes.Command;

/** Класс команды Exit, наследуется от Command */
public class Exit extends Command {

    /** Конструктор присваивает имя, описание и переменную isSingle */
    public Exit()
    {
        name = "exit";
        description = "завершить программу (без сохранения в файл)\n";
        isSingle = true;
    }

    /** Позволяет пользователю завершить программу */
    @Override
    public String execute() {

        //System.exit(0);
        System.out.println("Клиент отключился о сервера 0_0");
        return "Вы отключились от сервера -_-";
    }

}