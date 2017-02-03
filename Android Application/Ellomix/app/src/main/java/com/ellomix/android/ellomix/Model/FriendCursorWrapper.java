package com.ellomix.android.ellomix.Model;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.ellomix.android.ellomix.Messaging.Chat;

/**
 * Created by ATH-AJT2437 on 2/1/2017.
 */

public class FriendCursorWrapper extends CursorWrapper {
    public FriendCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String idString = getString(getColumnIndex(FriendDbSchema.FriendTable.Cols.ID));
        String name = getString(getColumnIndex(FriendDbSchema.FriendTable.Cols.PROFILE_NAME));
        String picture_url = getString(getColumnIndex(FriendDbSchema.FriendTable.Cols.PROFILE_PICTURE));

        User user = new User(idString, name, picture_url);

        return user;
    }
}
