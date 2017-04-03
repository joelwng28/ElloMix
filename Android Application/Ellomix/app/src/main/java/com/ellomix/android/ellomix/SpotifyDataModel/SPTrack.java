package com.ellomix.android.ellomix.SpotifyDataModel;

import com.ellomix.android.ellomix.Model.Sources;
import com.ellomix.android.ellomix.Model.Track;
import com.google.gson.annotations.SerializedName;

import javax.xml.transform.Source;

/**
 * Created by Akshay on 3/28/17.
 */

public class SPTrack extends Track {

    @SerializedName("name")
    private String mTitle;

    @SerializedName("uri")
    private String mStreamUri;

    public SPTrack() {
        super.setSource(Sources.SPOTIFY);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getStreamURL() {
        return mStreamUri;
    }

    public void setStreamURL(String uri) {
        mStreamUri = uri;
    }
}
