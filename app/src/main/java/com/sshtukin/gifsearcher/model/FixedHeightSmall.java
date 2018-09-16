package com.sshtukin.gifsearcher.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FixedHeightSmall {

    @SerializedName("url")
    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @SerializedName("width")
    @Expose
    private String width;

    public int getWidth() {
        return Integer.valueOf(width);
    }

    public void setWidth(String url) {
        this.width = width;
    }
}
