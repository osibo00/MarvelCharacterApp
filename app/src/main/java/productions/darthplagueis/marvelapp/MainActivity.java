package productions.darthplagueis.marvelapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import productions.darthplagueis.marvelapp.database.Character;
import productions.darthplagueis.marvelapp.database.CharacterDataBase;
import productions.darthplagueis.marvelapp.recyclerview.controller.CharacterAdapter;


public class MainActivity extends AppCompatActivity implements UtilityFragment.TaskStatusCallBack {

    private final String TAG = "Main_Activity";
    private FragmentManager fragmentManager;
    private LoadingFragment loadingFragment;
    private UtilityFragment utilityFragment;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        loadingFragment = new LoadingFragment();
        fragmentManager.beginTransaction()
                .add(R.id.main_container, loadingFragment)
                .commit();

        utilityFragment = (UtilityFragment) fragmentManager
                .findFragmentByTag(UtilityFragment.TAG_RETROFIT_FRAG);
        if (utilityFragment == null) {
            utilityFragment = new UtilityFragment();
            fragmentManager.beginTransaction()
                    .add(utilityFragment, UtilityFragment.TAG_RETROFIT_FRAG)
                    .commit();
        }

        if (utilityFragment != null) {
            utilityFragment.initializeDbCheck(CharacterDataBase.getDataBase(this));
        }

    }

    @Override
    protected void onDestroy() {
        CharacterDataBase.destroyInstance();
        super.onDestroy();
    }

    @Override
    public void passDatabaseStatus(Boolean status) {
        if (utilityFragment != null) {
            utilityFragment.getCharacters(CharacterDataBase.getDataBase(this), status);
        }
    }

    @Override
    public void passCharacterList(List<Character> characterList) {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new CharacterAdapter(characterList));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        fragmentManager.beginTransaction()
                .remove(loadingFragment)
                .commit();
    }
}
