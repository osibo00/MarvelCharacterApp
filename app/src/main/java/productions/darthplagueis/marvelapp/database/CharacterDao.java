package productions.darthplagueis.marvelapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


@Dao
@TypeConverters(DateConverter.class)
public interface CharacterDao {

    @Query("SELECT * FROM character")
    LiveData<List<Character>> getAll();

    @Query("SELECT * FROM character where download_Date > :after")
    LiveData<List<Character>> findCharactersAfter(Date after);

    @Query("SELECT * FROM character where character_name LIKE :name")
    Character findByName(String name);

    @Query("SELECT COUNT(*) from character")
    int countCharacters();

    @Query("DELETE FROM character")
    void deleteAll();

    @Insert(onConflict = REPLACE)
    void insertAll(Character... characters);

    @Delete
    void delete(Character character);
}
