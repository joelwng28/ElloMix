package com.ellomix.android.ellomix;

/**
 * Created by abetorres on 12/15/16.
 */

public class User {

    private String mName;
    private String mDescription;
    private int mFollowersCount;
    private int mFollowingCount;
    private String mPhotoUrl;

    public User(String name, String description, int followers, int following, String photoUrl) {
        mName = name;
        mDescription = description;
        mFollowersCount = followers;
        mFollowingCount = following;
        mPhotoUrl = photoUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        mDescription = mDescription;
    }

    public int getmFollowersCount() {
        return mFollowersCount;
    }

    public void incrementFollowersCount() {
        mFollowersCount++;
    }

    public void decrementFollowersCount() {
        mFollowersCount--;
    }

    public int getFollowingCount() {
        return mFollowingCount;
    }

    public void incrementFollowingCount() {
        mFollowingCount++;
    }

    public void decrementFollowingCount() {
        mFollowingCount--;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }
}
