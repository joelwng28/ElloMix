package com.ellomix.android.ellomix.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ellomix.android.ellomix.Model.MusicDbSchema.MusicTable;

/**
 * Created by ATH-AJT2437 on 2/12/2017.
 */

public class MusicBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "musicBase.db";

    public MusicBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MusicTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                MusicTable.Cols.ID + ", " +
                MusicTable.Cols.ARTIST  + ", " +
                MusicTable.Cols.TITLE + ", " +
                MusicTable.Cols.DATE + ", " +
                MusicTable.Cols.ARTWORK_URL + ", " +
                MusicTable.Cols.STREAM_URL + ", " +
                MusicTable.Cols.SOURCE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
