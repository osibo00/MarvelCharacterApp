package productions.darthplagueis.marvelapp.database;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class CharacterViewModel extends AndroidViewModel {

    public final LiveData<List<Character>> characterList;

    public CharacterViewModel(@NonNull Application application) {
        super(application);
        CharacterDataBase dataBase = CharacterDataBase.getDataBase(this.getApplication());
        characterList = dataBase.characterDao().getAll();
    }
}
