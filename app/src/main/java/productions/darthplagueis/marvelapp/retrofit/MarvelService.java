package productions.darthplagueis.marvelapp.retrofit;

import productions.darthplagueis.marvelapp.model.MarvelResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by oleg on 1/30/18.
 */

public interface MarvelService {

    @GET("v1/public/characters")
    Call<MarvelResults> getMarvelResults(@Query("ts") String timeStamp, @Query("apikey") String apiKey, @Query("hash") String hash);

    @GET("v1/public/characters/1011334")
    Call<MarvelResults> gethhhResults(@Query("ts") String timeStamp, @Query("apikey") String apiKey, @Query("hash") String hash);
}
