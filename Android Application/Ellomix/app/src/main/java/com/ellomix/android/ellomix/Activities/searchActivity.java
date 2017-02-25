package com.ellomix.android.ellomix.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ellomix.android.ellomix.Model.Track;
import com.ellomix.android.ellomix.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.api.services.youtube.model.SearchResultSnippet;

import java.util.List;

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
    public void processFinish(List<Track> outputResult) {

    }
}
