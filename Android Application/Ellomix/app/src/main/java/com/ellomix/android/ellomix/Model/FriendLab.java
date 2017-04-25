package com.ellomix.android.ellomix.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

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
        mContext.deleteDatabase(FriendDbSchema.FriendTable.NAME);
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
                FriendDbSchema.FriendTable.Cols.ID + " = ?",
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

        mDatabase.insert(FriendDbSchema.FriendTable.NAME, null, values);
    }

    public void updateFriend(User friend) {
        String idString = friend.getId();
        ContentValues values = getContentValues(friend);

        mDatabase.update(FriendDbSchema.FriendTable.NAME, values,
                FriendDbSchema.FriendTable.Cols.ID + " = ?",
                new String[] {idString});
    }

    private static ContentValues getContentValues(User friend) {
        ContentValues values = new ContentValues();
        values.put(FriendDbSchema.FriendTable.Cols.ID, friend.getId());
        values.put(FriendDbSchema.FriendTable.Cols.PROFILE_NAME, friend.getName());
        values.put(FriendDbSchema.FriendTable.Cols.PROFILE_PICTURE, friend.getPhotoUrl());

        return values;
    }

    private FriendCursorWrapper queryFriend(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                FriendDbSchema.FriendTable.NAME,
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
