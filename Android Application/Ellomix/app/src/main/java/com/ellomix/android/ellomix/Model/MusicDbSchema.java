package com.ellomix.android.ellomix.Model;

/**
 * Created by ATH-AJT2437 on 2/12/2017.
 */

public class MusicDbSchema {
    public static final class MusicTable {
        public static final String NAME = "music";

        public static final class Cols {
            public static final String ID = "id";
            public static final String ARTIST = "artist";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String ARTWORK_URL = "artwork_url";
            public static final String STREAM_URL = "stream_url";
            public static final String SOURCE = "source";
        }
    }
}
