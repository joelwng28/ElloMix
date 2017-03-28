package com.ellomix.android.ellomix.SpotifyAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Akshay on 3/28/17.
 */

public class SPTracks {
    @SerializedName("items")
    private List<SPTracks> mListItems;

    public void setItems(List<SPTracks> items) {
        mListItems = items;
    }

    public List<SPTracks> getItems() {
        return mListItems;
    }
}
