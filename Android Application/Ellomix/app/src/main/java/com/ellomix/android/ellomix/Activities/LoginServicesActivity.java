package com.ellomix.android.ellomix.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.Services.PlayerLab;
import com.ellomix.android.ellomix.SpotifyAPI.SpotifyAPI;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

//import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class LoginServicesActivity extends AppCompatActivity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback {

    private static final String TAG = "LoginServicesActivity";
    private static final String Extra_Initial_Setup = "InitialSetup";

    private Button mSpotifyButton;
    private SpotifyPlayer mPlayer;
    private PlayerLab mPlayerLab;
    private boolean isInitialSetup = false;

    public static Intent newIntent(Context context, boolean isInitialSetup) {
        Intent i = new Intent(context, LoginServicesActivity.class);
        i.putExtra(Extra_Initial_Setup, isInitialSetup);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_services);

        mPlayerLab = (PlayerLab) getApplicationContext();

        mSpotifyButton = (Button) findViewById(R.id.spotify_button);

        Intent i = getIntent();
        isInitialSetup = i.getBooleanExtra(Extra_Initial_Setup, true);

        mSpotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPlayerLab.isSpotifyConnected()) {
                    //TODO: Create confirmation dialog for log off
                    AuthenticationRequest.Builder builder =
                            new AuthenticationRequest.Builder(SpotifyAPI.getClientId(),
                                    AuthenticationResponse.Type.TOKEN,
                                    SpotifyAPI.getRedirectUri());
                    builder.setScopes(new String[]{"streaming"});
                    AuthenticationRequest request = builder.build();

                    AuthenticationClient.openLoginActivity(LoginServicesActivity.this,
                            SpotifyAPI.getRequestCode(), request);
                }
            }
        });

        updateUI();
    }

    private void updateUI() {
        if (mPlayerLab.isSpotifyConnected()) {
            mSpotifyButton.setText("Connected");
            mSpotifyButton.setEnabled(false);
        }
        else {
            mSpotifyButton.setText("Spotify");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.connected_services_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.continue_menu_item:
                if (isInitialSetup) {
                    goToHomeScreen();
                }
                else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                            mPlayer.addConnectionStateCallback(LoginServicesActivity.this);
                            mPlayer.addNotificationCallback(LoginServicesActivity.this);
                            updateUI();
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

    private void goToHomeScreen() {
        Intent i = new Intent(this, ScreenSlidePagerActivity.class);
        startActivity(i);
        finish();
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
        updateUI();
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
