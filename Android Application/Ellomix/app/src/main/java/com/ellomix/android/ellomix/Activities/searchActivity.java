package com.ellomix.android.ellomix.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ellomix.android.ellomix.R;
import com.google.android.youtube.player.YouTubeBaseActivity;

/**
 * Created by Abhi on 2/4/2017.
 */

public class searchActivity extends YouTubeBaseActivity implements youtubeSearchAPI.AsyncResponse{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_search);
    }

    @Override
    public void processFinish(String output) {
        Log.i("callback::processFinish", output);
        //Do shit with the output here
    }



}
