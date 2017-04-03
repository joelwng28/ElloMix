package com.ellomix.android.ellomix.Activities;

import android.os.Bundle;

import com.ellomix.android.ellomix.Model.Track;
import com.ellomix.android.ellomix.R;
import com.google.android.youtube.player.YouTubeBaseActivity;

import java.util.List;

/**
 * Created by Abhi on 2/4/2017.
 */

public class searchActivity extends YouTubeBaseActivity implements youtubeSearchAPI.AsyncResponse{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.fragment_search);
    }

    @Override
    public void processFinish(List<Track> outputResult) {

    }
}
