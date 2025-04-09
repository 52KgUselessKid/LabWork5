package Interfaces;

import Managers.CollectionManager;

public interface CCream {

    default void execute() {}

    default void execute(CollectionManager collectionManager, String[] args) {}

    default void execute(CollectionManager collectionManager){}

    String getName();

    String getDescription();

}