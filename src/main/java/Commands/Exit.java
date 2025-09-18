package Commands;

import Classes.Command;
import Net.Server;

/** Класс команды Exit, наследуется от Command */
public class Exit extends Command {

    /** Конструктор присваивает имя, описание и переменную isSingle */
    public Exit()
    {
        name = "exit";
        description = "завершить программу (без сохранения в файл)\n";
        isSingle = true;
        argCount = 1;
    }

    /** Позволяет пользователю завершить программу */
    @Override
    public String execute(int uid) {
        Server.logger.info("Клиент отключился о сервера 0_0");
        return "Вы отключились от сервера x_x";
    }

}