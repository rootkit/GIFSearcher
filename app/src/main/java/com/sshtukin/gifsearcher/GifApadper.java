package com.sshtukin.gifsearcher;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.sshtukin.gifsearcher.model.Datum;

import java.util.ArrayList;
import java.util.List;

public class GifApadper extends RecyclerView.Adapter<GifApadper.GifHolder>{

    private List<Datum> mDatumList;
    private Context mContext;

    public GifApadper(Context context){
        mContext = context;
        mDatumList = new ArrayList<>();
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
                    .into(holder.mImageView);

            holder.mImageView.getLayoutParams().width = Utils.convertDpToPixel(mDatumList.get(position).getImages().getFixedHeightSmall().getWidth(), mContext);

            ViewGroup.LayoutParams lp = holder.mImageView.getLayoutParams();
            if (lp instanceof FlexboxLayoutManager.LayoutParams){
                FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
                flexboxLp.setFlexGrow(1.0f);
            }
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

    public void addItems(List<Datum> datumList) {
        mDatumList.addAll(datumList);
    }

    public void wipeItems() {
        mDatumList = new ArrayList<>();
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
            PreviewDialog previewDialog = new PreviewDialog(mContext, mDatumList.get(getLayoutPosition()).getImages().getOriginal().getUrl());
            previewDialog.show();
        }
    }
}
