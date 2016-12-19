package com.ellomix.android.ellomix.SoundCloudDataModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abetorres on 11/17/16.
 */

public class SCUser {

    @SerializedName("id")
    private String mId;

    @SerializedName("username")
    private String mUserName;

    @SerializedName("kind")
    private String mType;

    @SerializedName("avatar_url")
    private String mAvatarUrl;

    public String getUserName() {
        return mUserName;
    }
}
