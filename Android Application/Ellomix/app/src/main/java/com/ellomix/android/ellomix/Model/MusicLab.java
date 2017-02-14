package com.ellomix.android.ellomix.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ellomix.android.ellomix.Model.MusicDbSchema.MusicTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATH-AJT2437 on 2/12/2017.
 */

public class MusicLab {
    private static MusicLab sMusicLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static MusicLab get(Context context) {
        if (sMusicLab == null) {
            sMusicLab = new MusicLab(context);
        }
        return sMusicLab;
    }

    private MusicLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new MusicBaseHelper(mContext)
                .getWritableDatabase();
    }

    public List<Track> getTracks() {
        List<Track> tracks = new ArrayList<>();

        MusicCursorWrapper cursor = queryTrack(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tracks.add(cursor.getTrack());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return tracks;
    }

    public Track getTrack(String id) {
        MusicCursorWrapper cursor = queryTrack(
                ChatDbSchema.ChatTable.Cols.ID + " = ?",
                new String[] {id}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getTrack();
        }
        finally {
            cursor.close();
        }
    }

    public void addTrack(Track t) {
        ContentValues values = getContentValues(t);

        mDatabase.insert(MusicTable.NAME, null, values);
    }

    public void updateTrack(Track track) {
        String idString = track.getID();
        ContentValues values = getContentValues(track);

        mDatabase.update(MusicTable.NAME, values,
                MusicTable.Cols.ID + " = ?",
                new String[] {idString});
    }

    //TODO: Implement remove method

    //TODO: Be sure that the song removes itself after 3 days

    private static ContentValues getContentValues(Track track) {
        ContentValues values = new ContentValues();
        values.put(MusicTable.Cols.ID, track.getID());
        values.put(MusicTable.Cols.DATE, track.getCreatedAt());
        values.put(MusicTable.Cols.ARTIST, track.getArtist());
        values.put(MusicTable.Cols.TITLE, track.getTitle());
        values.put(MusicTable.Cols.STREAM_URL, track.getStreamURL());
        values.put(MusicTable.Cols.ARTWORK_URL, track.getArtworkURL());
        values.put(MusicTable.Cols.SOURCE, track.getSource().toString());

        return values;
    }

    private MusicCursorWrapper queryTrack(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                MusicTable.NAME,
                null, //Columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new MusicCursorWrapper(cursor);
    }

}
