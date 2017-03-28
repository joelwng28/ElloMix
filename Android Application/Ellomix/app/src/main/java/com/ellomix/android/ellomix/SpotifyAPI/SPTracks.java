package com.ellomix.android.ellomix.SpotifyAPI;

import com.ellomix.android.ellomix.SpotifyDataModel.SPTrack;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Akshay on 3/28/17.
 */

public class SPTracks {
    @SerializedName("items")
    private List<SPTrack> mListItems;

    public void setItems(List<SPTrack> items) {
        mListItems = items;
    }

    public List<SPTrack> getItems() {
        return mListItems;
    }
}
