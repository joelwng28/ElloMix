package com.ellomix.android.ellomix.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abetorres on 12/15/16.
 */

public class User {

    private String mId;
    private String mName;
    private String mDescription;
    private int mFollowersCount;
    private int mFollowingCount;
    private String mPhotoUrl;
    private List<String> mChatIds;
    private List<String> mFollowingIds;

    public User(){

    }

    public User(String name) {
        mName = name;
        mDescription = "";
        mFollowersCount = 0;
        mFollowingCount = 0;
        mPhotoUrl = "";
        mChatIds = new ArrayList<>();
        mFollowingIds = new ArrayList<>();
    }

    public User(String name, String description, int followers, int following, String photoUrl) {
        mName = name;
        mDescription = description;
        mFollowersCount = followers;
        mFollowingCount = following;
        mPhotoUrl = photoUrl;
        mChatIds = new ArrayList<>();
        mFollowingIds = new ArrayList<>();;
    }

    public User(String id, String name, String photoUrl) {
        mId = id;
        mName = name;
        mDescription = "";
        mFollowersCount = 0;
        mFollowingCount = 0;
        mPhotoUrl = photoUrl;
        mChatIds = new ArrayList<>();
        mFollowingIds = new ArrayList<>();
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

    public void addChat(String chatId) {
        mChatIds.add(chatId);
    }

    public void removeChat(String chatId) {
        for (int i = 0; i < mChatIds.size(); i++) {
            if (mChatIds.get(i).equals(chatId)) {
                mChatIds.remove(i);
            }
        }
    }

    public List<String> getChat() {
        return mChatIds;
    }

    public void addFollowing(String followingId) {
        mFollowingIds.add(followingId);
    }

    public void removeFollowing(String followingId) {
        for (int i = 0; i < mFollowingIds.size(); i++) {
            if (mFollowingIds.get(i).equals(followingId)) {
                mFollowingIds.remove(i);
            }
        }
    }
}
