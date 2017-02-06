package com.ellomix.android.ellomix.Messaging;

import com.ellomix.android.ellomix.Model.Track;
import com.ellomix.android.ellomix.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by abetorres on 12/8/16.
 */

public class Chat {

    private String mId;
    private String mFromRecipient;
    private HashMap<String, String> mGroupMembers;
    private String mMostRecentMessage;
    private HashMap<String, Track> mGroupPlaylist;
    private int currentSongIndex;
    // TODO: add playlist list

    public Chat() {
        mId = "";
        mFromRecipient = "From";
        mMostRecentMessage = "Start messaging with your friends";
        mGroupMembers = new HashMap<>();
        mGroupPlaylist = new HashMap<>();
        currentSongIndex = 0;
    }

    public Chat(String id) {
        mId = id;
        mFromRecipient = "From";
        mMostRecentMessage = "Start messaging with your friends";
        mGroupMembers = new HashMap<>();
        mGroupPlaylist = new HashMap<>();
        currentSongIndex = 0;
    }

    public Chat(String id, HashMap<String, String> group) {
        mId = id;
        mFromRecipient = "From";
        mMostRecentMessage = "Long Long Time Ago...";
        mGroupMembers = group;
        mGroupPlaylist = new HashMap<>();
        currentSongIndex = 0;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getFromRecipient() {
        return mFromRecipient;
    }

    public String getMostRecentMessage() {
        return mMostRecentMessage;
    }

    public void setMostRecentMessage(String latestMessage) {
        mMostRecentMessage = latestMessage;
    }

    public void setFromRecipient(String name) {
        mFromRecipient = name;
    }

    /*
        Add new member to the chat
        return false if it fails to add or is already on group
        else true
     */
    public boolean addGroupMember(User user) {
        if (user == null) {
            return false;
        }
        if (!mGroupMembers.containsKey(user.getId())) {
            mGroupMembers.put(user.getId(), user.getName());
            return true;
        }
        else {
            return false;
        }
    }

    /*
        Remove member from chat
        return false if it fails to remove or is not in chat
        else true
     */
    public void removeGroupMember(String userId) {
        if (userId == null) {
            return;
        }
        mGroupMembers.remove(userId);
    }

    public void addMusic(Track track) {
        String indexString = currentSongIndex + "";
        mGroupPlaylist.put(indexString, track);
        currentSongIndex++;
    }

    public HashMap<String, Track> getGroupPlaylist() {
        return mGroupPlaylist;
    }

}
