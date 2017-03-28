package com.ellomix.android.ellomix.SpotifyAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Akshay on 3/28/17.
 */

public class SpotifyResponse {

    @SerializedName("tracks")
    private SPTracks mTracks;

    public SPTracks getTracks() {
        return mTracks;
    }

    public void setTracks(SPTracks tracks) {
        mTracks = tracks;
    }

}
