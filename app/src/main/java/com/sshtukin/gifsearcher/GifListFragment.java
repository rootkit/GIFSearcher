package com.sshtukin.gifsearcher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TabHost;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.sshtukin.gifsearcher.model.Datum;
import com.sshtukin.gifsearcher.model.GiphyModel;

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
    private final int GIF_NUMBER = 25;

    private RecyclerView mRecyclerView;
    private GifApadper mGifApadper;
    private SearchView mSearchView;
    private GiphyApi mClient;
    private Retrofit mRetrofit;
    private int mOffset;
    private final int TRENDING_MODE = 0;
    private final int SEARCH_MODE = 1;
    private int mCurrentMode;
    private String mRequest;
    private boolean isScrolling;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mOffset = 0;
        mCurrentMode = -1;
        mRequest = null;
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
        View view = inflater.inflate(R.layout.fragment_gif_list, container, false);
        setHasOptionsMenu(true);

        final FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setAlignItems(AlignItems.STRETCH);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        if (mGifApadper == null) {
            mGifApadper = new GifApadper(getActivity());
            mRetrofit = RetrofitClient.getClient(BASE_URL);
            mClient = mRetrofit.create(GiphyApi.class);
            loadGifs(TRENDING_MODE, null);
        }
        else {
            mGifApadper.updateContext(getActivity());
        }
        mRecyclerView.setAdapter(mGifApadper);

        isScrolling = false;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isScrolling&& (layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition()
                        == layoutManager.getItemCount())){
                    isScrolling = false;
                    mOffset += GIF_NUMBER;
                    loadGifs(mCurrentMode, mRequest);
                }
            }
        });

        return view;
    }

    void loadGifs(int mode, String s){
        if (mCurrentMode != mode){
            mOffset = 0;
            mGifApadper.wipeItems();
            mCurrentMode = mode;
        } else {
            mOffset += 25;
        }

        if (mode == TRENDING_MODE){
            callEnqueue(mClient.getTrending(API_KEY, mOffset));
        }
        if (mode == SEARCH_MODE){
            callEnqueue(mClient.getSearch(API_KEY, s, mOffset));
            mRequest = s;
        }
    }

    void callEnqueue(Call call) {
        call.enqueue(new Callback<GiphyModel>() {
            @Override
            public void onResponse(Call<GiphyModel> call, Response<GiphyModel> response) {
                GiphyModel giphyModel = response.body();
                List<Datum> datumList = giphyModel.getData();
                mGifApadper.addItems(datumList);
                mGifApadper.notifyDataSetChanged();
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
                loadGifs(SEARCH_MODE, s);
                mSearchView.clearFocus();
                mRecyclerView.getLayoutManager().scrollToPosition(0);
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
                loadGifs(TRENDING_MODE, null);
                mSearchView.onActionViewCollapsed();
                mRecyclerView.getLayoutManager().scrollToPosition(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
