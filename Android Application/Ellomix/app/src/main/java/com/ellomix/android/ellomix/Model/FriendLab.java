package com.ellomix.android.ellomix.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import com.ellomix.android.ellomix.Model.FriendDbSchema.FriendTable;

/**
 * Created by ATH-AJT2437 on 2/2/2017.
 */

public class FriendLab {
    private static FriendLab sFriendLab;

    private List<User> mFriends;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static FriendLab get(Context context) {
        if (sFriendLab == null) {
            sFriendLab = new FriendLab(context);
        }
        return sFriendLab;
    }

    private FriendLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new FriendBaseHelper(mContext)
                .getWritableDatabase();
        mFriends = new ArrayList<>();
    }

    public void deleteDatabase() {
        mDatabase.delete(FriendTable.NAME,
                null, null);
    }

    public List<User> getFriends() {
        List<User> friends = new ArrayList<>();
        FriendCursorWrapper cursor = queryFriend(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                friends.add(cursor.getUser());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return friends;
    }

    public User getFriend(String id) {

        FriendCursorWrapper cursor = queryFriend(
                FriendTable.Cols.ID + " = ?",
                new String[] {id}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getUser();
        }
        finally {
            cursor.close();
        }
    }

    public void addFriend(User c) {
        ContentValues values = getContentValues(c);

        mDatabase.insert(FriendTable.NAME, null, values);
    }

    public void updateFriend(User friend) {
        String idString = friend.getId();
        ContentValues values = getContentValues(friend);

        mDatabase.update(FriendTable.NAME, values,
                FriendTable.Cols.ID + " = ?",
                new String[] {idString});
    }

    private static ContentValues getContentValues(User friend) {
        ContentValues values = new ContentValues();
        values.put(FriendTable.Cols.ID, friend.getId());
        values.put(FriendTable.Cols.PROFILE_NAME, friend.getName());
        values.put(FriendTable.Cols.PROFILE_PICTURE, friend.getPhotoUrl());

        return values;
    }

    private FriendCursorWrapper queryFriend(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                FriendTable.NAME,
                null, //Columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new FriendCursorWrapper(cursor);
    }
}
