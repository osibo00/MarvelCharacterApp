package productions.darthplagueis.marvelapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by oleg on 1/31/18.
 */
@Dao
public interface CharacterDao {

    @Query("SELECT * FROM character")
    List<Character> getAll();

    @Query("SELECT * FROM character where character_name LIKE :name")
    Character findByName(String name);

    @Query("SELECT COUNT(*) from character")
    int countCharacters();

    @Insert
    void insertAll(Character... characters);

    @Delete
    void delete(Character character);
}
