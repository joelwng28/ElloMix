package com.ellomix.android.ellomix;

import android.support.v4.app.Fragment;

public class ChatActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return ChatFragment.newInstance();
    }
}
