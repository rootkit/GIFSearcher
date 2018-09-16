package com.sshtukin.gifsearcher;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.sshtukin.gifsearcher.model.Datum;

import java.util.ArrayList;
import java.util.List;

public class GifRecyclerViewApadper extends RecyclerView.Adapter<GifRecyclerViewApadper.GifHolder>{

    private List<Datum> mDatumList;
    private Context mContext;

    public GifRecyclerViewApadper(Context context){
        mContext = context;
        mDatumList = new ArrayList<>();
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
                    .load(mDatumList.get(position).getImages().getFixedHeightSmall().getUrl())
                    .placeholder(R.drawable.ic_loading)
                    .into(holder.mImageView);

            holder.mImageView.getLayoutParams().width = convertDpToPixel(mDatumList.get(position).getImages().getFixedHeightSmall().getWidth(), mContext);

            ViewGroup.LayoutParams lp = holder.mImageView.getLayoutParams();
            if (lp instanceof FlexboxLayoutManager.LayoutParams){
                FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
                flexboxLp.setFlexGrow(1.0f);
            }
        }
    }

    public static int convertDpToPixel(int dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return Math.round(px);
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

    public class GifHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mImageView;

        public GifHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.gif_item, parent, false));
            mImageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PreviewGif previewGif = new PreviewGif(mContext, mDatumList.get(getLayoutPosition()).getImages().getOriginal().getUrl());
            previewGif.show();
        }
    }

}
