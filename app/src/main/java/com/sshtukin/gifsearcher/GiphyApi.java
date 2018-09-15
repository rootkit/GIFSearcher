package com.sshtukin.gifsearcher;
import com.sshtukin.gifsearcher.model.GiphyModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GiphyApi {

    @GET("/v1/gifs/trending")
    Call<GiphyModel> getTrending(@Query("api_key")String apiKey);

    @GET("/v1/gifs/search")
    Call<GiphyModel> getSearch(@Query("api_key")String apiKey,
                               @Query("q")String query);


}
