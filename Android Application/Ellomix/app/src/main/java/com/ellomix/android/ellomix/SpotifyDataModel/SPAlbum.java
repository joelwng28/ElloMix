package com.ellomix.android.ellomix.SpotifyDataModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Akshay on 4/24/17.
 */

public class SPAlbum {
    @SerializedName("images")
    private List<SPImages> mImages;

    public SPAlbum(){}

    public List<SPImages> getImages() {
        return mImages;
    }

    public void setImages(List<SPImages> images) {
        this.mImages = images;
    }
}
