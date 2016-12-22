package com.ellomix.android.ellomix.SoundCloudDataModel;

import com.ellomix.android.ellomix.Model.Track;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alepena01 on 10/27/16.
 */

public class SCTrack extends Track {

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
    private SCUser mSCUser;

    /* Precondition: Assume the */
    public SCTrack() {
        super();
        setArtist(mSCUser.getUserName());
    }
}
