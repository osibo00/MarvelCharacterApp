package productions.darthplagueis.marvelapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import productions.darthplagueis.marvelapp.database.Character;
import productions.darthplagueis.marvelapp.database.CharacterDataBase;
import productions.darthplagueis.marvelapp.database.CharacterViewModel;
import productions.darthplagueis.marvelapp.database.DatabaseInitializer;
import productions.darthplagueis.marvelapp.database.TypeConvertersViewModel;
import productions.darthplagueis.marvelapp.recyclerview.controller.CharacterAdapter;


public class MainActivity extends AppCompatActivity implements UtilityFragment.TaskStatusCallBack {

    private final String TAG = "Main_Activity";
    private FragmentManager fragmentManager;
    private LoadingFragment loadingFragment;
    private UtilityFragment utilityFragment;
    private RecyclerView recyclerView;
    private CharacterAdapter adapter;
    private GridLayoutManager layoutManager;
    private CharacterViewModel viewModel;
    private TypeConvertersViewModel convertersViewModel;
    private boolean hasAppLoaded;
    private boolean loadMoreCharacters;
    private int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This is placed here to test if a retrofit call is made when the database doesn't exist yet.//
        DatabaseInitializer.removeCharacters(CharacterDataBase.getDataBase(this));

        fragmentManager = getSupportFragmentManager();

        utilityFragment = (UtilityFragment) fragmentManager
                .findFragmentByTag(UtilityFragment.TAG_UTILITY_FRAG);
        if (utilityFragment == null) {
            utilityFragment = new UtilityFragment();
            fragmentManager.beginTransaction()
                    .add(utilityFragment, UtilityFragment.TAG_UTILITY_FRAG)
                    .commit();
        }

        if (!hasAppLoaded) {
            loadingFragment = new LoadingFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.main_container, loadingFragment)
                    .commit();
            if (utilityFragment != null) {
                utilityFragment.initializeDbCheck(CharacterDataBase.getDataBase(this));
            }
        }

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new CharacterAdapter();
        recyclerView.setAdapter(adapter);
        layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        setScrollListener();

        viewModel = ViewModelProviders.of(this).get(CharacterViewModel.class);
        convertersViewModel = ViewModelProviders.of(this).get(TypeConvertersViewModel.class);

    }

    @Override
    protected void onDestroy() {
        CharacterDataBase.destroyInstance();
        super.onDestroy();
    }

    @Override
    public void passDatabaseStatus(Boolean status) {
        if (status) {
            showCharactersInUi();
            removeLoadingFragment();
        } else {
            if (utilityFragment != null) {
                utilityFragment.setIsExecuting(false);
                utilityFragment.makeRetrofitCall(offset);
            }
        }
    }

    @Override
    public void retrofitCallComplete() {
        addCharactersToUi();
        removeLoadingFragment();
    }

    private void showCharactersInUi() {
        viewModel.characterList.observe(this, new Observer<List<Character>>() {
            @Override
            public void onChanged(@Nullable final List<Character> characterList) {
                updateRecyclerView(characterList);
                loadMoreCharacters = true;
                if (utilityFragment != null) {
                    utilityFragment.setIsExecuting(false);
                }
            }
        });
    }

    private void addCharactersToUi() {
        convertersViewModel.characterList.observe(this, new Observer<List<Character>>() {
            @Override
            public void onChanged(@Nullable final List<Character> characterList) {
                updateRecyclerView(characterList);
                Log.d(TAG, "onChanged: " + String.valueOf(characterList.size()));
                loadMoreCharacters = true;
                if (utilityFragment != null) {
                    utilityFragment.setIsExecuting(false);
                }
            }
        });
    }

    private void removeLoadingFragment() {
        if (!hasAppLoaded) {
            hasAppLoaded = true;
            fragmentManager.beginTransaction()
                    .remove(loadingFragment)
                    .commit();
        }
    }

    private void updateRecyclerView(final List<Character> characterList) {
        adapter.passCharacterList(characterList);
    }

    private void setScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if (dy > 0) {
                    if (loadMoreCharacters) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loadMoreCharacters = false;
                            offset+=30;
                            if (utilityFragment != null) {
                                utilityFragment.makeRetrofitCall(offset);
                                Log.d(TAG, "onScrolled: retrofit call made, offset: " + String.valueOf(offset));
                                Log.d(TAG, "onScrolled: totalItemCount: " + String.valueOf(totalItemCount));
                            }
                        }
                    }
                }
            }
        });
    }
}
