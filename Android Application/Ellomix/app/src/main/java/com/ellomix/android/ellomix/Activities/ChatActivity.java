package com.ellomix.android.ellomix.Activities;

import android.support.v4.app.Fragment;

import com.ellomix.android.ellomix.Fragments.ChatFragment;

public class ChatActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return ChatFragment.newInstance();
    }
}
