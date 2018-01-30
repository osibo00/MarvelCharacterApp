package productions.darthplagueis.marvelapp.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by oleg on 1/30/18.
 */

public class MarvelRetrofit {

    private Retrofit retrofit;

    private static MarvelRetrofit instance;

    private MarvelRetrofit() {
        String marvelBaseUrl = "https://gateway.marvel.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(marvelBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static MarvelRetrofit getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new MarvelRetrofit();
        return instance;
    }

    public MarvelService getMarvelService() {
        return retrofit.create(MarvelService.class);
    }
}
