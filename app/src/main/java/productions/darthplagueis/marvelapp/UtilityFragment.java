package productions.darthplagueis.marvelapp;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import productions.darthplagueis.marvelapp.database.CharacterDataBase;
import productions.darthplagueis.marvelapp.database.DatabaseInitializer;
import productions.darthplagueis.marvelapp.model.MarvelResults;
import productions.darthplagueis.marvelapp.model.marvelserviceresults.CharacterResults;
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

    public void initializeDbCheck(@NonNull final CharacterDataBase dataBase) {
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

        MarvelService marvelService = MarvelRetrofit.getInstance().getMarvelService();
        Call<MarvelResults> call = marvelService.getThirtyResults(30, offset, timeStamp, API_KEY, hashValue);
        call.enqueue(new Callback<MarvelResults>() {
            @Override
            public void onResponse(Call<MarvelResults> call, Response<MarvelResults> response) {
                Log.d(TAG_UTILITY_FRAG, "onResponse: Call Request " + call.request());
                if (response.isSuccessful()) {
                    MarvelResults marvelResults = response.body();
                    List<CharacterResults> characterResultsList = marvelResults.getData().getResults();
                    DatabaseInitializer.populateAsync(CharacterDataBase.getDataBase(getContext()), characterResultsList);
                    Log.d(TAG_UTILITY_FRAG, "onResponse: list size " + characterResultsList.size());
                    Log.d(TAG_UTILITY_FRAG, "onResponse: list " + response.raw());
                }
                callBack.retrofitCallComplete();
                Log.d(TAG_UTILITY_FRAG, "onResponse:  Status " + response.message() + " " + response.code() + " "
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
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
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

    private static class CheckDatabase extends AsyncTask<Void, Void, Boolean> {

        private final CharacterDataBase db;

        CheckDatabase(CharacterDataBase dataBase) {
            db = dataBase;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d(UtilityFragment.TAG_UTILITY_FRAG, "doInBackground: " + String.valueOf(db.characterDao().countCharacters() >= 20));
            return db.characterDao().countCharacters() >= 20;
        }

        @Override
        protected void onPostExecute(Boolean hasDatabase) {
            callBack.passDatabaseStatus(hasDatabase);
        }
    }

    public interface TaskStatusCallBack {
        void passDatabaseStatus(Boolean status);

        void retrofitCallComplete();
    }
}
