package com.ellomix.android.ellomix;

import android.support.v4.app.Fragment;

public class TimelineActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return TimelineFragment.newInstance();
    }
}
