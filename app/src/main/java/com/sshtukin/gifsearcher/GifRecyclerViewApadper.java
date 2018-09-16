package com.sshtukin.gifsearcher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sshtukin.gifsearcher.model.Datum;

import java.util.List;

public class GifRecyclerViewApadper extends RecyclerView.Adapter<GifRecyclerViewApadper.GifHolder>{

    private List<Datum> mDatumList;
    private Context mContext;

    public GifRecyclerViewApadper(Context context, List<Datum> datumList){
        mContext = context;
        mDatumList = datumList;
    }

    public void setItems(List<Datum> datumList){
        mDatumList = datumList;
    }

    public void updateContext(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public GifHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return new GifHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final GifHolder holder, int position) {
        if (mDatumList.size() > 0) {
            GlideApp
                    .with(mContext)
                    .load(mDatumList.get(position).getImages().getOriginal().getUrl())
                    .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.mProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.mProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                    .into(holder.mImageView);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDatumList.size();
    }

    public class GifHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        ProgressBar mProgressBar;


        public GifHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.gif_item, parent, false));
            mImageView = itemView.findViewById(R.id.imageView);
            mProgressBar = itemView.findViewById(R.id.progress);
        }
    }

}
