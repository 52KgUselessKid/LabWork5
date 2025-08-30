package Interfaces;

import Classes.MusicBand;
import Managers.CollectionManager;

/** Интерфейс CCream, содержит поведение для Command объектов */
public interface CCream {

    default String execute() {return null;}

    default String execute(CollectionManager collectionManager, String[] args) {return null;}

    default String execute(CollectionManager collectionManager){return null;}

    default String execute(CollectionManager collectionManager, MusicBand mb){return null;}

    default String execute(CollectionManager collectionManager, String[] args, Object object){return null;}

    String getName();

    String getDescription();

    int getCommArgCount();
}