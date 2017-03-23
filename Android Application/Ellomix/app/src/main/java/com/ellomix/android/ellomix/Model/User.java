package com.ellomix.android.ellomix.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abetorres on 12/15/16.
 */

public class User implements Parcelable {

    private String mId;
    private String mName;
    private String mDescription;
    private int mFollowersCount;
    private int mFollowingCount;
    private String mPhotoUrl;
//    private List<String> mChatIds;
//    private List<String> mFollowingIds;

    public User(){

    }

    public User(Parcel in) {
        this.mId = in.readString();
        this.mName = in.readString();
        this.mDescription = in.readString();
        this.mFollowersCount = in.readInt();
        this.mFollowingCount = in.readInt();
        this.mPhotoUrl = in.readString();
    }

    public User(String name) {
        mName = name;
        mDescription = "";
        mFollowersCount = 0;
        mFollowingCount = 0;
        mPhotoUrl = "";
    }

    public User(String name, String description, int followers, int following, String photoUrl) {
        mName = name;
        mDescription = description;
        mFollowersCount = followers;
        mFollowingCount = following;
        mPhotoUrl = photoUrl;
    }

    public User(String id, String name, String photoUrl) {
        mId = id;
        mName = name;
        mDescription = "";
        mFollowersCount = 0;
        mFollowingCount = 0;
        mPhotoUrl = photoUrl;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
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

    public int getFollowersCount() {
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

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

//    public void addChat(String chatId) {
//        mChatIds.add(chatId);
//    }
//
//    public void removeChat(String chatId) {
//        for (int i = 0; i < mChatIds.size(); i++) {
//            if (mChatIds.get(i).equals(chatId)) {
//                mChatIds.remove(i);
//            }
//        }
//    }
//
//    public List<String> getChat() {
//        return mChatIds;
//    }
//
//    public void addFollowing(String followingId) {
//        mFollowingIds.add(followingId);
//    }
//
//    public void removeFollowing(String followingId) {
//        for (int i = 0; i < mFollowingIds.size(); i++) {
//            if (mFollowingIds.get(i).equals(followingId)) {
//                mFollowingIds.remove(i);
//            }
//        }
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeInt(mFollowersCount);
        dest.writeInt(mFollowingCount);
        dest.writeString(mPhotoUrl);
    }

    static final Parcelable.Creator<User> CREATOR =
            new Parcelable.Creator<User>() {

                @Override
                public User createFromParcel(Parcel source) {
                    return new User(source);
                }

                @Override
                public User[] newArray(int size) {
                    return new User[size];
                }
            };
}
