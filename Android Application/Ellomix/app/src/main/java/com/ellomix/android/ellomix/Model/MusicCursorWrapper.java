package com.ellomix.android.ellomix.Model;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.ellomix.android.ellomix.Model.MusicDbSchema.MusicTable;


/**
 * Created by ATH-AJT2437 on 2/12/2017.
 */

public class MusicCursorWrapper extends CursorWrapper {

    public MusicCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Track getTrack() {
        String idString = getString(getColumnIndex(MusicTable.Cols.ID));
        String titleString = getString(getColumnIndex(MusicTable.Cols.TITLE));
        String artistString = getString(getColumnIndex(MusicTable.Cols.ARTIST));
        String dateString = getString(getColumnIndex(MusicTable.Cols.DATE));
        String artworkString = getString(getColumnIndex(MusicTable.Cols.ARTWORK_URL));
        String streamString = getString(getColumnIndex(MusicTable.Cols.STREAM_URL));
        String sourceString = getString(getColumnIndex(MusicTable.Cols.SOURCE));

        Track track = new Track(titleString, artistString, idString);
        track.setStreamURL(streamString);
        track.setArtworkURL(artworkString);
        track.setCreatedAt(dateString);
        track.setSource(Sources.valueOf(sourceString));

        return track;
    }
}
