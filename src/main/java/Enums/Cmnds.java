package Enums;

import Classes.Command;
import Interfaces.CCream;
import Commands.*;

public enum Cmnds {
    add(new Add()),
    addpro(new AddPro()),
    clear(new Clear()),
    execute(new Execute_script()),
    exit(new Exit()),
    filter(new Filter_sw_name()),
    group_count(new Group_count()),
    head(new Head()),
    help(new Help()),
    info(new Info()),
    load(new Load()),
    print_numofparts(new Print_numOfParts()),
    remove(new Remove()),
    remove_head(new Remove_head()),
    remove_lower(new Remove_lower()),
    save(new Save()),
    show(new Show()),
    update(new Update()),
    updatepro(new UpdatePro());

    final Command command;

    Cmnds(Command command)
    {
    this.command = command;
    }

    public Command getCommand()
    {
        return command;
    }

}
