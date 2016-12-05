package com.ellomix.android.ellomix;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChatFeedActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return ChatFeedFragment.newInstance();
    }
}
