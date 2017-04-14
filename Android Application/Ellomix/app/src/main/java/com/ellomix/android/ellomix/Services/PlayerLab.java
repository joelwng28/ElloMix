package com.ellomix.android.ellomix.Services;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import com.ellomix.android.ellomix.SpotifyAPI.SpotifyAPI;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

/**
 * Created by Akshay on 4/10/17.
 */

public class PlayerLab extends Application {

    private static final String TAG = "PlayerLab";

    private static PlayerLab singleton;

    private static final String STATUS = "ConnectedStatus";
    private static final String SpotifyStatus = "Spotify";
    private SharedPreferences mConnectedServices;

    // SpotifyAPI instance variable
    private Player mPlayer;

    public static PlayerLab getInstance(){
        return singleton;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        mConnectedServices = getSharedPreferences(STATUS, 0);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        Spotify.destroyPlayer(this);
        super.onTerminate();
    }

    //Check connected status

    public boolean isSpotifyConnected() {
        mConnectedServices = getSharedPreferences(STATUS, 0);
        return mConnectedServices.getBoolean(SpotifyStatus, false);
    }

    //Update connected status

    public void updateSpotifyStatus(boolean spStatus) {
        mConnectedServices = getSharedPreferences(STATUS, 0);
        SharedPreferences.Editor editor = mConnectedServices.edit();
        editor.putBoolean(SpotifyStatus, spStatus);
        editor.apply();
    }

    //SpotifyAPI methods

    public void setupSpotifyPlayer(Player player) {
        if (player != null) {
            mPlayer = player;
            updateSpotifyStatus(true);
        }
    }
    public boolean isSpotifyLoggedIn() {
        return mPlayer != null;
    }

    public void spotifyLogOut() {
        Spotify.destroyPlayer(this);
        mPlayer = null;
    }

//    // SpotifyAPI player callback
//    @Override
//    public void onLoggedIn() {
//        Log.d(TAG, "User logged in");
//    }
//
//    @Override
//    public void onLoggedOut() {
//        Log.d(TAG, "User logged out");
//    }
//
//    @Override
//    public void onLoginFailed(Error error) {
//        Log.d(TAG, "Login failed");
//    }
//
//    @Override
//    public void onTemporaryError() {
//        Log.d(TAG, "Temporary error occurred");
//    }
//
//    @Override
//    public void onConnectionMessage(String s) {
//        Log.d(TAG, "Received connection message: " + s);
//    }
//
//    @Override
//    public void onPlaybackEvent(PlayerEvent playerEvent) {
//        Log.d(TAG, "Playback event received: " + playerEvent.name());
//        switch (playerEvent) {
//            // Handle event type as necessary
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onPlaybackError(Error error) {
//        Log.d(TAG, "Playback error received: " + error.name());
//        switch (error) {
//            // Handle error type as necessary
//            default:
//                break;
//        }
//    }
}
