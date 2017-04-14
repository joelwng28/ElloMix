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


public class SplashActivity extends AppCompatActivity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback {

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
                if (playerLab.isSpotifyConnected()) {
                    AuthenticationRequest.Builder builder =
                            new AuthenticationRequest.Builder(SpotifyAPI.getClientId(),
                                    AuthenticationResponse.Type.TOKEN,
                                    SpotifyAPI.getRedirectUri());
                    builder.setScopes(new String[]{"streaming"});
                    AuthenticationRequest request = builder.build();

                    AuthenticationClient.openLoginActivity(SplashActivity.this,
                            SpotifyAPI.getRequestCode(), request);
                }

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
                    i = new Intent(SplashActivity.this, LoginServicesActivity.class);
                }

                finish();
                startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == SpotifyAPI.getRequestCode()) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    Log.i(TAG, "Login successful");
                    // Setup player
                    Config playerConfig = new Config(this, response.getAccessToken(), SpotifyAPI.getClientId());
                    Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                        @Override
                        public void onInitialized(SpotifyPlayer spotifyPlayer) {
                            Log.d(TAG, "Initialize player");
                            mPlayer = spotifyPlayer;
                            mPlayer.addConnectionStateCallback(SplashActivity.this);
                            mPlayer.addNotificationCallback(SplashActivity.this);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Log.e(TAG, "Could not initialize player: " + throwable.getMessage());
                        }
                    });
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Log.e(TAG, "Auth error: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
                    Log.i(TAG, "Auth result: " + response.getType());
            }
        }
    }

    // SpotifyAPI player NotificationCallback

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d(TAG, "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d(TAG, "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    // SpotifyAPI player ConnectionStateCallback

    @Override
    public void onLoggedIn() {
        Log.d(TAG, "User logged in");
        PlayerLab playerLab = (PlayerLab) getApplicationContext();
        playerLab.setupSpotifyPlayer(mPlayer);
    }

    @Override
    public void onLoggedOut() {
        Log.d(TAG, "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d(TAG, "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d(TAG, "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d(TAG, "Received connection message: " + message);
    }
}
