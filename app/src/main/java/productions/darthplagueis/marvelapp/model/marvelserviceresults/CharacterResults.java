package productions.darthplagueis.marvelapp.model.marvelserviceresults;

import java.util.List;

public class CharacterResults {

    private int id;
    private String name;
    private CharacterThumbnail thumbnail;
    private CharacterComics comics;
    private List<CharacterUrls> urls;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CharacterThumbnail getThumbnail() {
        return thumbnail;
    }

    public CharacterComics getComics() {
        return comics;
    }

    public List<CharacterUrls> getUrls() {
        return urls;
    }
}
