package productions.darthplagueis.marvelapp.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MarvelRetrofit {

    private Retrofit retrofit;

    private static MarvelRetrofit INSTANCE;

    private MarvelRetrofit() {
        String marvelBaseUrl = "https://gateway.marvel.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(marvelBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static MarvelRetrofit getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MarvelRetrofit();
        }
        return INSTANCE;
    }

    public MarvelService getMarvelService() {
        return retrofit.create(MarvelService.class);
    }
}
