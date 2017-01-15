package com.ellomix.android.ellomix.Activities;

import android.support.v4.app.Fragment;
import com.ellomix.android.ellomix.Fragments.CommentsFragment;


public class CommentsActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return CommentsFragment.newInstance();
    }
}
