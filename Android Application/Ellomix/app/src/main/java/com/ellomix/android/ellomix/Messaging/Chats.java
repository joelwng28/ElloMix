package com.ellomix.android.ellomix.Messaging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abetorres on 12/13/16.
 */

public class Chats {

    private String mId;
    private List<Chat> mChats;
    private String mUser;

    public Chats(String id, String user) {
        mId = id;
        mUser = user;
        mChats = new ArrayList<>(5);
    }

    //Precondition newChat is never null
    public void addChat(Chat newChat) {
        mChats.add(newChat);
    }

    public boolean removeChat(int position) {
        if (position >= mChats.size()) {
            return false;
        }

        mChats.remove(position);
        return true;
    }

}
