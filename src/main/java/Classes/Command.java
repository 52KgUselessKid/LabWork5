package Classes;

import Interfaces.CCream;

public abstract class Command implements CCream {

    protected String name, description;

    public boolean isSingle, cllOnly;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return name + " -- " + description;
    }
}