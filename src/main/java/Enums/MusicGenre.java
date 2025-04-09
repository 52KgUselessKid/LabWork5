package Enums;

public enum MusicGenre {
    PSYCHEDELIC_ROCK("PSYCHEDELIC_ROCK"),
    JAZZ("JAZZ"),
    MATH_ROCK("MATH_ROCK"),
    POST_PUNK("POST_PUNK"),
    BRIT_POP("BRIT_POP");

    final String name;

    MusicGenre(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}