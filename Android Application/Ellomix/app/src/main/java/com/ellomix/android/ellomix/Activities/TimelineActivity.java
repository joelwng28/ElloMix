package com.ellomix.android.ellomix.Activities;

import android.support.v4.app.Fragment;

import com.ellomix.android.ellomix.Fragments.TimelineFragment;

public class TimelineActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return TimelineFragment.newInstance();
    }
}