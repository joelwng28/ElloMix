package com.ellomix.android.ellomix;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

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
