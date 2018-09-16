package com.sshtukin.gifsearcher;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.widget.ImageView;

public class PreviewDialog extends Dialog {
    private ImageView mImageView;
    private String mUrl;
    private Context mContext;

    public PreviewDialog(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_preview);
        mImageView = findViewById(R.id.image_preview);
        GlideApp
                .with(mContext)
                .load(mUrl)
                .into(mImageView);
    }
}