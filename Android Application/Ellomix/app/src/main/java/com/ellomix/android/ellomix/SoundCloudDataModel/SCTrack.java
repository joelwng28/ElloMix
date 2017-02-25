package com.ellomix.android.ellomix.SoundCloudDataModel;

import com.ellomix.android.ellomix.Model.Sources;
import com.ellomix.android.ellomix.Model.Track;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alepena01 on 10/27/16.
 */

public class SCTrack extends Track {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("id")
    private String mID;

    @SerializedName("created_at")
    private String mCreatedAt;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("artwork_url")
    private String mArtworkURL;

    @SerializedName("duration")
    private String mTime;

    @SerializedName("user")
    private SCUser mSCUser;

    public String getArtist(){
        return mSCUser.getUserName();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getID() {
        return mID;
    }

    public void setID(String id) {
        mID = id;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public void setStreamURL(String url) {
        mStreamURL = url;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }

    public void setmArtworkURL(String artworkURL) {
        mArtworkURL = artworkURL;
    }

    public String getTime() {
        return mTime;
    }
}
