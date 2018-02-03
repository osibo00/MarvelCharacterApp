package productions.darthplagueis.marvelapp.retrofit;

import productions.darthplagueis.marvelapp.model.MarvelResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MarvelService {

    @GET("v1/public/characters")
    Call<MarvelResults> getDefaultResults(@Query("ts") String timeStamp, @Query("apikey") String apiKey, @Query("hash") String hash);

    @GET("v1/public/characters")
    Call<MarvelResults> getCharacterResults(@Query("limit") int limit, @Query("offset") int offset, @Query("ts") String timeStamp,
                                            @Query("apikey") String apiKey, @Query("hash") String hash);
}
