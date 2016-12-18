package com.ellomix.android.ellomix.SoundCloudDataModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alepena01 on 10/27/16.
 */

public class Track {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("id")
    private int mID;

    @SerializedName("created_at")
    private String mCreatedAt;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("artwork_url")
    private String mArtworkURL;

    @SerializedName("duration")
    private String mTime;

    @SerializedName("user")
    private User mUser;

    public String getTitle() {
        return mTitle;
    }

    public int getID() {
        return mID;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }

    public String getTime() {
        return mTime;
    }

    public User getUser() {
        return mUser;
    }

}
