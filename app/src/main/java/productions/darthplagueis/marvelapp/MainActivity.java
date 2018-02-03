package productions.darthplagueis.marvelapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import productions.darthplagueis.marvelapp.database.Character;
import productions.darthplagueis.marvelapp.database.CharacterDatabase;
import productions.darthplagueis.marvelapp.database.CharacterViewModel;
import productions.darthplagueis.marvelapp.database.DatabaseInitializer;
import productions.darthplagueis.marvelapp.recyclerview.controller.CharacterAdapter;
import productions.darthplagueis.marvelapp.recyclerview.view.CharacterViewHolder;


public class MainActivity extends AppCompatActivity implements UtilityFragment.TaskStatusCallBack {

    private final String TAG = "Main_Activity";
    private final String KEY_LOAD_MORE_CHARS = "load_More_Characters";
    private final String KEY_HAS_APP_LOADED = "has_App_Loaded";
    private final String KEY_OFFSET = "Offset";
    private SharedPreferences preferences;
    private FragmentManager fragmentManager;
    private LoadingFragment loadingFragment;
    private UtilityFragment utilityFragment;
    private RecyclerView recyclerView;
    private CharacterAdapter adapter;
    private GridLayoutManager layoutManager;
    private CharacterViewModel viewModel;
    //private TypeConvertersViewModel convertersViewModel;
    private boolean hasAppLoaded;
    private boolean loadMoreCharacters;
    private int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This is placed here to test if a retrofit call is made when the database hasn't been populated yet //
        //DatabaseInitializer.removeCharacters(CharacterDatabase.getDataBase(this));

        preferences = getSharedPreferences("Shared_Prefs", Context.MODE_PRIVATE);
        offset = preferences.getInt(KEY_OFFSET, 0);
        Log.d(TAG, "onCreate Offset: " + offset);

        if (savedInstanceState != null) {
            loadMoreCharacters = savedInstanceState.getBoolean(KEY_LOAD_MORE_CHARS);
            hasAppLoaded = savedInstanceState.getBoolean(KEY_HAS_APP_LOADED);
        }

        fragmentManager = getSupportFragmentManager();

        utilityFragment = (UtilityFragment) fragmentManager
                .findFragmentByTag(UtilityFragment.TAG_UTILITY_FRAG);
        if (utilityFragment == null) {
            utilityFragment = new UtilityFragment();
            fragmentManager.beginTransaction()
                    .add(utilityFragment, UtilityFragment.TAG_UTILITY_FRAG)
                    .commit();
        }

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new CharacterAdapter();
        recyclerView.setAdapter(adapter);

        if (!hasAppLoaded) {
            loadingFragment = new LoadingFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.main_container, loadingFragment)
                    .commit();
        }

        recyclerView.setHasFixedSize(true);
        setScrollListener();
        setSwipeListener();

        viewModel = ViewModelProviders.of(this).get(CharacterViewModel.class);
        //convertersViewModel = ViewModelProviders.of(this).get(TypeConvertersViewModel.class);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_LOAD_MORE_CHARS, loadMoreCharacters);
        outState.putBoolean(KEY_HAS_APP_LOADED, hasAppLoaded);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        }
        recyclerView.setLayoutManager(layoutManager);
        if (utilityFragment != null) {
            utilityFragment.initializeDbCheck(CharacterDatabase.getDataBase(this));
        }
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        CharacterDatabase.destroyInstance();
        super.onDestroy();
    }

    @Override
    public void passDatabaseStatus(Boolean status) {
        if (status) {
            showCharactersInUi();
        } else {
            if (utilityFragment != null) {
                utilityFragment.setIsExecuting(false);
                utilityFragment.makeRetrofitCall(offset);
            }
        }
    }

    @Override
    public void passToAdapter(List<Character> characterList) {
        updateRecyclerView(characterList);
    }

    @Override
    public void retrofitCallComplete() {
        loadMoreCharacters = true;
        if (utilityFragment != null) {
            utilityFragment.setIsExecuting(false);
        }
        removeLoadingFragment();
    }

    private void showCharactersInUi() {
        viewModel.initializeDb();
        viewModel.getCharacterList().observe(this, new Observer<List<Character>>() {
            @Override
            public void onChanged(@Nullable final List<Character> characterList) {
                updateRecyclerView(characterList);
                loadMoreCharacters = true;
                if (utilityFragment != null) {
                    utilityFragment.setIsExecuting(false);
                }
                removeLoadingFragment();
            }
        });
    }

    private void updateRecyclerView(List<Character> characterList) {
        adapter.passCharacterList(characterList);
    }

//    private void addCharactersToUi() {
//        convertersViewModel.characterList.observe(this, new Observer<List<Character>>() {
//            @Override
//            public void onChanged(@Nullable final List<Character> characterList) {
//                updateRecyclerView(characterList);
//                Log.d(TAG, "onChanged: " + String.valueOf(characterList.size()));
//                loadMoreCharacters = true;
//                if (utilityFragment != null) {
//                    utilityFragment.setIsExecuting(false);
//                }
//            }
//        });
//    }

    private void removeLoadingFragment() {
        if (!hasAppLoaded) {
            hasAppLoaded = true;
            fragmentManager.beginTransaction()
                    .remove(loadingFragment)
                    .commit();
            Toast.makeText(MainActivity.this, "Swipe to remove characters", Toast.LENGTH_LONG).show();
        }
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
                            offset += 30;
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt(KEY_OFFSET, offset);
                            editor.apply();
                            if (utilityFragment != null) {
                                utilityFragment.makeRetrofitCall(offset);
                                Log.d(TAG, "onScrolled: retrofit call made, offset: " + offset);
                                Log.d(TAG, "onScrolled: totalItemCount: " + totalItemCount);
                            }
                        }
                    }
                }
            }
        });
    }

    private void setSwipeListener() {
        ItemTouchHelper.SimpleCallback itemCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder instanceof CharacterViewHolder) {
                    Character character = ((CharacterViewHolder) viewHolder).getCharacter();
                    DatabaseInitializer.removeSpecificCharacter(CharacterDatabase.getDataBase(MainActivity.this), character);
                    Toast.makeText(MainActivity.this, "Character Removed", Toast.LENGTH_LONG).show();
                }
                int position = viewHolder.getAdapterPosition();
                adapter.removeCharacter(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
