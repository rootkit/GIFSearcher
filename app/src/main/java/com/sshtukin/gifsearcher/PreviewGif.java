package com.sshtukin.gifsearcher;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PreviewGif extends Dialog {
    private ImageView mImageView;
    private String mUrl;
    private Context mContext;

    public PreviewGif(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previev_gif);
        mImageView = findViewById(R.id.image_preview);
        GlideApp
                .with(mContext)
                .load(mUrl)
                .into(mImageView);
    }
}