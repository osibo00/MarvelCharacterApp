package productions.darthplagueis.marvelapp.database;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class CharacterViewModel extends AndroidViewModel {

    private LiveData<List<Character>> characterList;

    public CharacterViewModel(@NonNull Application application) {
        super(application);
        initializeDb();
    }

    public void initializeDb() {
        CharacterDatabase dataBase = CharacterDatabase.getDataBase(this.getApplication());
        characterList = dataBase.characterDao().getAll();
    }

    public LiveData<List<Character>> getCharacterList() {
        return characterList;
    }
}
