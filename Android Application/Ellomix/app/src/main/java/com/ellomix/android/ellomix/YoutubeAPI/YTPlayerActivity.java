package com.ellomix.android.ellomix.YoutubeAPI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ellomix.android.ellomix.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YTPlayerActivity extends YouTubeBaseActivity {

    private static final String EXTRA_VIDEO_URL = "videoUrl";

    private String videoUrl;
    private YouTubePlayer actualPlayer;

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener;

    private static final String apiKey = "AIzaSyCmL8ycwQoL1UDUz9EWpWHTq3hy3e7r2ck";

    public static Intent newIntent(Context context, String videoUrl) {
        Intent intent = new Intent(context, YTPlayerActivity.class);
        intent.putExtra(EXTRA_VIDEO_URL, videoUrl);
        return intent;
    }

    private String getttingTheDough() {
        return videoUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ytplayer);

        videoUrl = getIntent().getStringExtra(EXTRA_VIDEO_URL);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.videoPlayer);
        playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {
                Log.i("YouTubeStateChange", "onLoading");
            }

            @Override
            public void onLoaded(String s) {

                Log.i("YouTubeStateChange", "onLoaded");
            }

            @Override
            public void onAdStarted() {
                Log.i("YouTubeStateChange", "onAdStarted");
            }

            @Override
            public void onVideoStarted() {
                Log.i("YouTubeStateChange", "onVideoStarted");
            }

            @Override
            public void onVideoEnded() {
                Log.i("YouTubeStateChange", "onVideoEnded");
                // Play next video??
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {
                Log.e("YouTubeStateChange", "onError");
            }
        };
        onInitializedListener = new YouTubePlayer.OnInitializedListener(){
            @Override
            public void onInitializationSuccess (YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored){
                if (actualPlayer == null) {
                    actualPlayer = youTubePlayer;
                }
                youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
                youTubePlayer.loadVideo(getttingTheDough());
            }

            @Override
            public void onInitializationFailure (YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        youTubePlayerView.initialize(apiKey, onInitializedListener);
    }
}
