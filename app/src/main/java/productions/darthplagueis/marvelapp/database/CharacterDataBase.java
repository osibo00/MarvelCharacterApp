package productions.darthplagueis.marvelapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by oleg on 1/31/18.
 */
@Database(entities = {Character.class}, version = 1)
public abstract class CharacterDataBase extends RoomDatabase {

    private static CharacterDataBase INSTANCE;

    public abstract CharacterDao characterDao();

    public static CharacterDataBase getDataBase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CharacterDataBase.class, "character-database")
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
