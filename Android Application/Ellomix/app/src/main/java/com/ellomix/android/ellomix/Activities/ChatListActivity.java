package com.ellomix.android.ellomix.Activities;

import android.support.v4.app.Fragment;

import com.ellomix.android.ellomix.Fragments.ChatListFragment;

public class ChatListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return ChatListFragment.newInstance();
    }
}
