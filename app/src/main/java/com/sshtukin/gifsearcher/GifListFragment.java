package com.sshtukin.gifsearcher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sshtukin.gifsearcher.model.GiphyModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GifListFragment extends Fragment {
    private final String API_KEY = "rU7m46fXGCETcsNpS7TvE0WL8uEniLaS";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gif_list,
                container, false);

        Retrofit retrofit = RetrofitClient.getClient("https://api.giphy.com");


        GiphyApi client = retrofit.create(GiphyApi.class);
        Call<GiphyModel> call = client.getTrending(API_KEY);
        call.enqueue(new Callback<GiphyModel>() {
            @Override
            public void onResponse(Call<GiphyModel> call, Response<GiphyModel> response) {
                GiphyModel giphyModel = response.body();
                Log.d("TEST", giphyModel.getData().get(0).getImages().getOriginal().getUrl());
            }

            @Override
            public void onFailure(Call<GiphyModel> call, Throwable t) {

            }
        });

        return view;
    }
}
