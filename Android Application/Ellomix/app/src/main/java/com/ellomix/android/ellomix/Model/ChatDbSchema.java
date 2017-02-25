package com.ellomix.android.ellomix.Model;

import java.util.List;

/**
 * Created by ATH-AJT2437 on 1/24/2017.
 */

public class ChatDbSchema {
    public static final class ChatTable {
        public static final String NAME = "messages";

        public static final class Cols {
            public static final String ID = "id";
            public static final String FROM_RECIPIENT = "from_recipient";
            public static final String MOST_RECENT_MESSAGE = "most_recent_message";
        }
    }
}
