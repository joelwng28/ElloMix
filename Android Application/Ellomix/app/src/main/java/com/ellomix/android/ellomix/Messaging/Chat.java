package com.ellomix.android.ellomix.Messaging;

/**
 * Created by abetorres on 12/8/16.
 */

public class Chat {

    private String mId;
    private String mFromRecipient;
    private String mMostRecentMessage;

    public Chat(int id) {
        mId = String.valueOf(id);
    }

    public Chat(String id) {
        mId = id;
        mFromRecipient = "From";
        mMostRecentMessage = "Long Long Time Ago...";
    }

    public String getId() {
        return mId;
    }

    public void setMostRecentMessage(String latestMessage) {
        mMostRecentMessage = latestMessage;
    }

    public String getFromRecipient() {
        return mFromRecipient;
    }

    public String getMostRecentMessage() {
        return mMostRecentMessage;
    }
}
