package com.ellomix.android.ellomix.Activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;

import com.ellomix.android.ellomix.Fragments.ProfileFragment;

public class ProfileActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return ProfileFragment.newInstance();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
}
