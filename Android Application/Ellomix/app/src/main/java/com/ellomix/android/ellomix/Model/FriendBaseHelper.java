package com.ellomix.android.ellomix.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ATH-AJT2437 on 2/1/2017.
 */

public class FriendBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "friendBase.db";

    public FriendBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FriendDbSchema.FriendTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                FriendDbSchema.FriendTable.Cols.ID + ", " +
                FriendDbSchema.FriendTable.Cols.PROFILE_NAME + ", " +
                FriendDbSchema.FriendTable.Cols.PROFILE_PICTURE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
