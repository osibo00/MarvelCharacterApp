package productions.darthplagueis.marvelapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;


@Entity(tableName = "character")
@TypeConverters(DateConverter.class)
public class Character {

    @PrimaryKey
    private int characterId;

    @ColumnInfo(name = "download_date")
    private Date downloadDate;

    @ColumnInfo(name = "character_name")
    private String name;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

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

    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
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
