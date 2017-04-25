package com.ellomix.android.ellomix.SpotifyDataModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Akshay on 4/24/17.
 */

public class SPArtist {
    @SerializedName("name")
    private String mArtist;

    public SPArtist() {

    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        this.mArtist = artist;
    }
}
