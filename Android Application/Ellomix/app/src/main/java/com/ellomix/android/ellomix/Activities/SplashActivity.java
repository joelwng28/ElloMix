package com.ellomix.android.ellomix.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.Services.PlayerLab;
import com.ellomix.android.ellomix.SpotifyAPI.SpotifyAPI;
import com.facebook.AccessToken;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    private SpotifyPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final PlayerLab playerLab = (PlayerLab) getApplicationContext();

        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

        iv.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv.startAnimation(an2);

                Intent i;
                if(AccessToken.getCurrentAccessToken() == null)
                {
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }
                else {
                    //TODO: Testing
                    i = new Intent(SplashActivity.this, ScreenSlidePagerActivity.class);
                }

                finish();
                startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}
