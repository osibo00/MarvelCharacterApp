package productions.darthplagueis.marvelapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {Character.class}, version = 1)
public abstract class CharacterDatabase extends RoomDatabase {

    private static CharacterDatabase INSTANCE;

    public abstract CharacterDao characterDao();

    public static CharacterDatabase getDataBase(Context context) {
        if (INSTANCE == null) {
            String DATABASE_NAME = "Marvel_Characters_Database";
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CharacterDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
