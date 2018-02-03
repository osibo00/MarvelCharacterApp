package productions.darthplagueis.marvelapp.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TypeConvertersViewModel extends AndroidViewModel {

    public LiveData<List<Character>> characterList;

    public TypeConvertersViewModel(@NonNull Application application) {
        super(application);
        initializeDb();
    }

    private void initializeDb() {
        CharacterDatabase dataBase = CharacterDatabase.getDataBase(this.getApplication());
        characterList = dataBase.characterDao().findCharactersAfter(getDownloadDate());
    }

    private Date getDownloadDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        Log.d("DATE", "getDownloadDate: " + String.valueOf(calendar.getTime()));
        return calendar.getTime();
    }
}
