package com.ellomix.android.ellomix.Model;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.ellomix.android.ellomix.Messaging.Chat;
import com.ellomix.android.ellomix.Model.ChatDbSchema.ChatTable;

/**
 * Created by ATH-AJT2437 on 1/24/2017.
 */

public class ChatCursorWrapper extends CursorWrapper {
    public ChatCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Chat getChat() {
        String idString = getString(getColumnIndex(ChatTable.Cols.ID));
        String fromRecipient = getString(getColumnIndex(ChatTable.Cols.FROM_RECIPIENT));
        String recentMessage = getString(getColumnIndex(ChatTable.Cols.MOST_RECENT_MESSAGE));

        Chat chat = new Chat(idString);
        chat.setFromRecipient(fromRecipient);
        chat.setMostRecentMessage(recentMessage);

        return chat;
    }
}
