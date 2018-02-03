package productions.darthplagueis.marvelapp;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import productions.darthplagueis.marvelapp.database.Character;
import productions.darthplagueis.marvelapp.database.CharacterDatabase;
import productions.darthplagueis.marvelapp.database.DatabaseInitializer;
import productions.darthplagueis.marvelapp.model.MarvelResults;
import productions.darthplagueis.marvelapp.model.marvelserviceresults.CharacterResults;
import productions.darthplagueis.marvelapp.model.marvelserviceresults.CharacterUrls;
import productions.darthplagueis.marvelapp.retrofit.MarvelRetrofit;
import productions.darthplagueis.marvelapp.retrofit.MarvelService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static productions.darthplagueis.marvelapp.BuildConfig.API_KEY;
import static productions.darthplagueis.marvelapp.BuildConfig.PRIVATE_KEY;


public class UtilityFragment extends Fragment {

    public static final String TAG_UTILITY_FRAG = "Utility_Fragment";
    private static TaskStatusCallBack callBack;
    private boolean isExecuting = false;

    public UtilityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TaskStatusCallBack) {
            callBack = (TaskStatusCallBack) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TaskStatusCallBack");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callBack = null;
    }

    public void initializeDbCheck(@NonNull final CharacterDatabase dataBase) {
        if (!isExecuting) {
            CheckDatabase task = new CheckDatabase(dataBase);
            task.execute();
            isExecuting = true;
        }
    }

    public void makeRetrofitCall(int offset) {
        if (!isExecuting) {
            isExecuting = true;
            marvelCharacterCall(offset);
        }
    }

    public void setIsExecuting(Boolean executing) {
        isExecuting = executing;
    }

    private void marvelCharacterCall(int offset) {
        Long tsLong = System.currentTimeMillis() / 1000;
        String timeStamp = tsLong.toString();
        String md5Digest = timeStamp + PRIVATE_KEY + API_KEY;
        String hashValue = md5(md5Digest);

        // This API call requires a time stamp //
        // It also requires a hash value to be passed with the retrofit call created using md5 //
        MarvelService marvelService = MarvelRetrofit.getInstance().getMarvelService();
        Call<MarvelResults> call = marvelService.getCharacterResults(30, offset, timeStamp, API_KEY, hashValue);
        call.enqueue(new Callback<MarvelResults>() {
            @Override
            public void onResponse(Call<MarvelResults> call, Response<MarvelResults> response) {
                Log.d(TAG_UTILITY_FRAG, "onResponse: Call Request: " + call.request());
                if (response.isSuccessful()) {
                    MarvelResults marvelResults = response.body();
                    List<CharacterResults> characterResultsList = marvelResults.getData().getResults();
                    callBack.passToAdapter(createCharacters(characterResultsList));
                    DatabaseInitializer.populateAsync(CharacterDatabase.getDataBase(getContext()), characterResultsList);
                    Log.d(TAG_UTILITY_FRAG, "onResponse: List size: " + characterResultsList.size());
                    Log.d(TAG_UTILITY_FRAG, "onResponse: Json: " + response.raw());
                }
                callBack.retrofitCallComplete();
                Log.d(TAG_UTILITY_FRAG, "onResponse:  Status: " + response.message() + " " + response.code() + " "
                        + response.errorBody());
            }

            @Override
            public void onFailure(Call<MarvelResults> call, Throwable t) {
                isExecuting = false;
                t.printStackTrace();
            }
        });
    }

    private String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash //
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String //
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private List<Character> createCharacters(List<CharacterResults> resultsList) {
        List<Character> characterList = new ArrayList<>();
        for (CharacterResults result : resultsList) {
            Character character = new Character();
            character.setCharacterId(result.getId());
            character.setName(result.getName());
            character.setImageUrl(result.getThumbnail().getPath() + "." + result.getThumbnail().getExtension());
            character.setComicsAvail(result.getComics().getAvailable());

            List<CharacterUrls> characterUrls = result.getUrls();
            character.setType(characterUrls.get(0).getType());
            character.setCharactersUrl(characterUrls.get(0).getUrl());

            characterList.add(character);
        }
        return characterList;
    }

    private static class CheckDatabase extends AsyncTask<Void, Void, Boolean> {

        private final CharacterDatabase database;

        CheckDatabase(CharacterDatabase dataBase) {
            this.database = dataBase;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            // The number 30 is used because that is the list size returned from the initial retrofit call //
            boolean doesDbExist = database.characterDao().countCharacters() >= 30;
            Log.d(TAG_UTILITY_FRAG, "Check Database Async: " + doesDbExist);
            Log.d(TAG_UTILITY_FRAG, "Check Database Size: " + database.characterDao().countCharacters());
            return doesDbExist;
        }

        @Override
        protected void onPostExecute(Boolean hasDatabase) {
            callBack.passDatabaseStatus(hasDatabase);
        }
    }

    public interface TaskStatusCallBack {
        void passDatabaseStatus(Boolean status);

        void passToAdapter(List<Character> characterList);

        void retrofitCallComplete();
    }
}
