package com.ellomix.android.ellomix;

import android.support.v4.app.Fragment;

public class ChatListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return ChatListFragment.newInstance();
    }
}
