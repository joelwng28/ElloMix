package com.ellomix.android.ellomix.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ellomix.android.ellomix.R;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class LoginServicesActivity extends AppCompatActivity implements
        ConnectionStateCallback {

    private static final String CLIENT_ID = "8390a95e6e6a4236a4f40cca17f13150";
    private static final String REDIRECT_URI = "my-spotify-login-one://callback";
    private static final String TAG = "LoginServicesActivity";

    private Button mSpotifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_services);

        mSpotifyButton = (Button) findViewById(R.id.spotify_button);
        mSpotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                        AuthenticationResponse.Type.TOKEN,
                        REDIRECT_URI);
                builder.setScopes(new String[]{"user-read-private", "streaming"});
                AuthenticationRequest request = builder.build();

                AuthenticationClient.openLoginActivity(LoginServicesActivity.this, REQUEST_CODE, request);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Toast.makeText(this, "Token received", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLoggedIn() {
        Intent i = new Intent(this, ScreenSlidePagerActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onLoggedOut() {
        //nothing
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
    public void onConnectionMessage(String s) {
        Log.d(TAG, "Received connection message: " + s);
    }
}
