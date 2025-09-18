package Interfaces;

import Classes.MusicBand;
import Managers.CollectionManager;

/** Интерфейс CCream, содержит поведение для Command объектов */
public interface CCream {

    default String execute(int uid) {return null;}

    default String execute(CollectionManager collectionManager, String[] args, int uid) {return null;}

    default String execute(CollectionManager collectionManager, int uid){return null;}

    default String execute(CollectionManager collectionManager, MusicBand mb, int uid){return null;}

    default String execute(CollectionManager collectionManager, String[] args, Object object, int uid){return null;}

    String getName();

    String getDescription();

    int getCommArgCount();
}