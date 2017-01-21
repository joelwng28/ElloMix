package com.ellomix.android.ellomix.Messaging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abetorres on 12/8/16.
 */

public class Chat {

    private String mId;
    private String mFromRecipient;
    private List<String> mGroupMembers;
    private String mMostRecentMessage;
    // TODO: add playlist list

    public Chat() {
        mId = "";
        mFromRecipient = "From";
        mMostRecentMessage = "Long Long Time Ago...";
        mGroupMembers = new ArrayList<>();
    }

    public Chat(String id) {
        mId = id;
        mFromRecipient = "From";
        mMostRecentMessage = "Long Long Time Ago...";
        mGroupMembers = new ArrayList<>();
    }

    public Chat(String id, List<String> group) {
        mId = id;
        mFromRecipient = "From";
        mMostRecentMessage = "Long Long Time Ago...";
        mGroupMembers = group;
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
    public boolean addGroupMember(String name) {
        if (name == null) {
            return false;
        }
        mGroupMembers.add(name);
        //TODO: Need to check for duplicates
        return true;
    }

    /*
        Remove member from chat
        return false if it fails to remove or is not in chat
        else true
     */
    public boolean removeGroupMember(String name) {
        if (name == null) {
            return false;
        }

        mGroupMembers.remove(name);
        //TODO: Change when user class is added
//        for (int i = 0; i < mGroupMembers.size(); i++) {
//
//        }
        return true;
    }

}
