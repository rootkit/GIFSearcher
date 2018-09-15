package com.sshtukin.gifsearcher;

import android.content.ContentProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sshtukin.gifsearcher.model.Datum;

import java.util.List;

public class GifRecyclerViewApadper extends RecyclerView.Adapter<GifRecyclerViewApadper.GifHolder>{
    List<Datum> mDatumList;
    Context mContext;

    public GifRecyclerViewApadper(Context context, List<Datum> datumList){
        mContext = context;
        mDatumList = datumList;
    }

    public void setItems(List<Datum> datumList){
        mDatumList = datumList;
    }

    @NonNull
    @Override
    public GifHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return new GifHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull GifHolder holder, int position) {
        if (mDatumList.size() > 0) {
            GlideApp
                    .with(mContext)
                    .load(mDatumList.get(position).getImages().getOriginal().getUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return 25;
    }

    public class GifHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;

        public GifHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.git_item, parent, false));
            mImageView = itemView.findViewById(R.id.imageView);
        }
    }
}
