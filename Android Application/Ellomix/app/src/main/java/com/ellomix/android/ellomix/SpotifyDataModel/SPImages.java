package com.ellomix.android.ellomix.SpotifyDataModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Akshay on 4/24/17.
 */

public class SPImages {
    @SerializedName("height")
    int mHeight;

    @SerializedName("url")
    String mUrl;

    @SerializedName("width")
    int mWidth;

    public SPImages() {
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }
}
