package com.sshtukin.gifsearcher;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
    private final String TAG = "GifListFragment";
    private final String KEY = "AdapterState";

    private RecyclerView mRecyclerView;
    private GifRecyclerViewApadper mGifRecyclerViewApadper;
    private SearchView mSearchView;
    private GiphyApi mClient;
    private Retrofit mRetrofit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelable(KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null)
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(KEY));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gif_list,
                container, false);
        setHasOptionsMenu(true);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        if (mGifRecyclerViewApadper  == null) {
            mGifRecyclerViewApadper = new GifRecyclerViewApadper(getActivity(), new ArrayList<Datum>());
            mRetrofit = RetrofitClient.getClient(BASE_URL);
            mClient = mRetrofit.create(GiphyApi.class);
            call_enqueue(getCall());
        }
        else {
            mGifRecyclerViewApadper.updateContext(getActivity());
        }
        mRecyclerView.setAdapter(mGifRecyclerViewApadper);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.line_divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }

    Call getCall(){
        return mClient.getTrending(API_KEY);
    }

    Call getCall(String s){
        return mClient.getSearch(API_KEY, s);
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
                Log.e(TAG, "Called OnFailure", t);
                Snackbar.make(getView(), R.string.internet_availability,  Snackbar.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_gif_list, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                call_enqueue(getCall(s));
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_trending:
                call_enqueue(getCall());
                mSearchView.onActionViewCollapsed();
                mRecyclerView.getLayoutManager().scrollToPosition(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
