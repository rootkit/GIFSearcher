package com.sshtukin.gifsearcher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sshtukin.gifsearcher.model.Datum;
import com.sshtukin.gifsearcher.model.GiphyModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GifListFragment extends Fragment {

    private final String API_KEY = "rU7m46fXGCETcsNpS7TvE0WL8uEniLaS";
    private final String BASE_URL = "https://api.giphy.com";

    RecyclerView mRecyclerView;
    GifRecyclerViewApadper mGifRecyclerViewApadper;
    GiphyApi client;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gif_list,
                container, false);
        setHasOptionsMenu(true);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mGifRecyclerViewApadper = new GifRecyclerViewApadper(getActivity(), new ArrayList<Datum>());
        mRecyclerView.setAdapter(mGifRecyclerViewApadper);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        Retrofit retrofit = RetrofitClient.getClient(BASE_URL);
        client = retrofit.create(GiphyApi.class);
        call_enqueue(getCall());
        return view;
    }

    Call getCall(){
        return client.getTrending(API_KEY);
    }

    Call getCall(String s){
        return client.getSearch(API_KEY, s);
    }


    void call_enqueue(Call call) {
        call.enqueue(new Callback<GiphyModel>() {
            @Override
            public void onResponse(Call<GiphyModel> call, Response<GiphyModel> response) {
                GiphyModel giphyModel = response.body();
                List<Datum> datumList = giphyModel.getData();
                mGifRecyclerViewApadper.setItems(datumList);
                mGifRecyclerViewApadper.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<GiphyModel> call, Throwable t) {
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_gif_list, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                call_enqueue(getCall(s));
                searchView.onActionViewCollapsed();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}
