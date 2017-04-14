package com.ellomix.android.ellomix.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ellomix.android.ellomix.Fragments.SearchFragment;
import com.ellomix.android.ellomix.Model.Track;
import com.ellomix.android.ellomix.R;
import com.google.android.youtube.player.YouTubeBaseActivity;

import java.util.List;

/**
 * Created by Abhi on 2/4/2017.
 */

public class SearchActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return SearchFragment.newInstance();
    }
}
