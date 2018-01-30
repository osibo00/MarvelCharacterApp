package productions.darthplagueis.marvelapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import productions.darthplagueis.marvelapp.model.marvelserviceresults.CharacterResults;
import productions.darthplagueis.marvelapp.recyclerview.controller.CharacterAdapter;


public class MainActivity extends AppCompatActivity implements RetrofitFragment.TaskStatusCallBack {

    private final String TAG = "Main_Activity";
    private FragmentManager fragmentManager;
    private LoadingFragment loadingFragment;
    private RetrofitFragment retrofitFragment;
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

        retrofitFragment = (RetrofitFragment) fragmentManager
                .findFragmentByTag(RetrofitFragment.TAG_RETROFIT_FRAG);
        if (retrofitFragment == null) {
            retrofitFragment = new RetrofitFragment();
            fragmentManager.beginTransaction()
                    .add(retrofitFragment, RetrofitFragment.TAG_RETROFIT_FRAG)
                    .commit();
        }

        if (retrofitFragment != null) {
            retrofitFragment.startCharacterCall();
        }

        recyclerView = findViewById(R.id.recycler_view);

    }

    @Override
    public void passCharacterList(List<CharacterResults> characterResults) {
        recyclerView.setAdapter(new CharacterAdapter(characterResults));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        fragmentManager.beginTransaction()
                .remove(loadingFragment)
                .commit();
    }
}
