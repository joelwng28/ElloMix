package com.ellomix.android.ellomix.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ellomix.android.ellomix.Messaging.Chat;

import com.ellomix.android.ellomix.Model.ChatDbSchema.ChatTable;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATH-AJT2437 on 1/24/2017.
 */

public class ChatLab {
    private static ChatLab sChatLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ChatLab get(Context context) {
        if (sChatLab == null) {
            sChatLab = new ChatLab(context);
        }
        return sChatLab;
    }

    private ChatLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ChatBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void deleteDatabase() {
        mContext.deleteDatabase(ChatTable.NAME);
    }

    public List<Chat> getChats() {
        List<Chat> chats = new ArrayList<>();

        ChatCursorWrapper cursor = queryChat(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                chats.add(cursor.getChat());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return chats;
    }

    public Chat getChat(String id) {
        ChatCursorWrapper cursor = queryChat(
                ChatTable.Cols.ID + " = ?",
                new String[] {id}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getChat();
        }
        finally {
            cursor.close();
        }
    }

    public void addChat(Chat c) {
        ContentValues values = getContentValues(c);

        mDatabase.insert(ChatTable.NAME, null, values);
    }

    public void removeChat(Chat c) {
        String idString = c.getId();

        mDatabase.delete(ChatTable.NAME,
                ChatTable.Cols.ID + " = ?",
                new String[] {idString});
    }

    public void updateChat(Chat chat) {
        String idString = chat.getId();
        ContentValues values = getContentValues(chat);

        mDatabase.update(ChatTable.NAME, values,
                ChatTable.Cols.ID + " = ?",
                new String[] {idString});
    }

    private static ContentValues getContentValues(Chat chat) {
        ContentValues values = new ContentValues();
        values.put(ChatTable.Cols.ID, chat.getId());
        values.put(ChatTable.Cols.FROM_RECIPIENT, chat.getFromRecipient());
        values.put(ChatTable.Cols.MOST_RECENT_MESSAGE, chat.getMostRecentMessage());

        return values;
    }

    private ChatCursorWrapper queryChat(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ChatTable.NAME,
                null, //Columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new ChatCursorWrapper(cursor);
    }
}
