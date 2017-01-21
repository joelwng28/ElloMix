package com.ellomix.android.ellomix.Messaging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abetorres on 12/13/16.
 */

public class Chats {

    private List<Chat> mChats;

    public Chats() {
        mChats = new ArrayList<>(5);
    }

    public List<Chat> getChats() {
        return mChats;
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

    public Chat getChat(String chatId) {
        for (int i = 0; i < mChats.size(); i++) {
            Chat currentChat = mChats.get(i);
            if (currentChat.getId().equals(chatId)) {
                return currentChat;
            }
        }
        return null;
    }

    public void updateChat(String chatId, Chat newChat) {
        Chat chat = getChat(chatId);
        chat.setMostRecentMessage(newChat.getMostRecentMessage());
    }

}
