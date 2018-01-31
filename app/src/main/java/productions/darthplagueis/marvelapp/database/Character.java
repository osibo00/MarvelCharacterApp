package productions.darthplagueis.marvelapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by oleg on 1/31/18.
 */
@Entity(tableName = "character")
public class Character {

    @PrimaryKey(autoGenerate = true)
    private int characterId;

    @ColumnInfo(name = "character_name")
    private String name;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "character_number")
    private int number;

    @ColumnInfo(name = "comics_available")
    private int comicsAvail;

    @ColumnInfo(name = "characters_type")
    private String type;

    @ColumnInfo(name = "characters_url")
    private String charactersUrl;

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getComicsAvail() {
        return comicsAvail;
    }

    public void setComicsAvail(int comicsAvail) {
        this.comicsAvail = comicsAvail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCharactersUrl() {
        return charactersUrl;
    }

    public void setCharactersUrl(String charactersUrl) {
        this.charactersUrl = charactersUrl;
    }
}
