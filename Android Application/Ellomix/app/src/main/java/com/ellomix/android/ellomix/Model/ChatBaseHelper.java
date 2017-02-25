package com.ellomix.android.ellomix.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ellomix.android.ellomix.Model.ChatDbSchema.ChatTable;

/**
 * Created by ATH-AJT2437 on 1/24/2017.
 */

public class ChatBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "messageBase.db";

    public ChatBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ChatTable.NAME + "(" +
        " _id integer primary key autoincrement, " +
        ChatTable.Cols.ID + ", " +
        ChatTable.Cols.FROM_RECIPIENT + ", " +
        ChatTable.Cols.MOST_RECENT_MESSAGE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
