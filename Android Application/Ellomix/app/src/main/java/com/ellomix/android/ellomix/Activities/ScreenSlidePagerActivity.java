package com.ellomix.android.ellomix.Activities;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;

import com.ellomix.android.ellomix.Fragments.ChatListFragment;
import com.ellomix.android.ellomix.Fragments.ProfileFragment;
import com.ellomix.android.ellomix.Fragments.SearchFragment;
import com.ellomix.android.ellomix.Model.MusicController;
import com.ellomix.android.ellomix.Model.MusicLab;
import com.ellomix.android.ellomix.Model.Track;
import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.Services.MusicService;
import com.ellomix.android.ellomix.Services.PlayerLab;
import com.ellomix.android.ellomix.SpotifyAPI.SpotifyAPI;
import com.ellomix.android.ellomix.Style.CustomViewPager;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;
import android.widget.MediaController.MediaPlayerControl;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by abetorres on 12/10/16.
 */

public class ScreenSlidePagerActivity extends AppCompatActivity implements MediaPlayerControl,
        ConnectionStateCallback {

    private static final String TAG = "PagerActivity";
    private static final int NUM_PAGES = 3;

    private CustomViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private int[] imageResId = {
            // R.drawable.ic_home,
            R.drawable.ic_search,
            R.drawable.ic_messsages,
            R.drawable.ic_profile
    };
    private MusicService musicService;
    private boolean musicBound = false;
    private MusicController mController;
    private SpotifyPlayer mPlayer;
    private PlaybackState mPlaybackState;
    private ProgressDialog mProgressDialog;
    private MusicLab mMusicLab;
    private PlayerLab mPlayerLab;
    private Track mCurrentTrack;
    private List<Track> mPlaylist;
    private Intent playIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        //TODO: Figure how to fix bug that cause view to disappear when changing between pages
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (CustomViewPager) findViewById(R.id.fragment_view_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);
        for (int i = 0; i < NUM_PAGES; i++) {
            tabLayout.getTabAt(i).setIcon(imageResId[i]);
        }
        mProgressDialog = new ProgressDialog(this);

        mPlayerLab = (PlayerLab) getApplicationContext();
        if (mPlayerLab.isSpotifyConnected() && !mPlayerLab.isSpotifyLoggedIn()) {
            showProcessDialog("Preparing Music Experience");
            AuthenticationRequest.Builder builder =
                    new AuthenticationRequest.Builder(SpotifyAPI.getClientId(),
                            AuthenticationResponse.Type.TOKEN,
                            SpotifyAPI.getRedirectUri());
            builder.setScopes(new String[]{"streaming"});
            AuthenticationRequest request = builder.build();

            AuthenticationClient.openLoginActivity(ScreenSlidePagerActivity.this,
                    SpotifyAPI.getRequestCode(), request);

        }
        mPlayerLab.createMediaPlayer();
        mPlaylist = new ArrayList<Track>();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if result comes from the correct activity
        if (requestCode == SpotifyAPI.getRequestCode()) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);
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
                            mPlayer.addConnectionStateCallback(ScreenSlidePagerActivity.this);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayerLab.isSpotifyConnected()) {
            setController();
        }
    }

    private void setController() {
        if (mController == null) {
            mController = new MusicController(this, false);
        }
        mController.setMediaPlayer(this);
        mController.setAnchorView(mPager);
        mController.setEnabled(true);
        mPlayerLab.setController(mController);
    }

    public void showProcessDialog(String message) {
        mProgressDialog.show();
        mProgressDialog.setMessage(message);
    }

    public void hideProcessDialog() {
        mProgressDialog.hide();
    }

//    //connect to the service
//    private ServiceConnection musicConnection = new ServiceConnection(){
//
//        @Override
//        public void onServiceConnected (ComponentName name, IBinder service) {
//            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
//            //get service
//            musicService = binder.getService();
//            //pass list
//            musicService.setList(mPlaylist);
//            musicService.setController(mController);
//            musicBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            musicBound = false;
//        }
//    };

    //MediaPlayer callback methods

    @Override
    public void start() {
        mPlayerLab.resumeTrack();
    }

    @Override
    public void pause() {
        mPlayerLab.pauseTrack();
    }

    @Override
    public int getDuration() {
        return mPlayerLab.getTrackDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mPlayerLab.getTrackCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mPlayerLab.seekTrackTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mPlayerLab.isTrackPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    // SpotifyAPI player ConnectionStateCallback

    @Override
    public void onLoggedIn() {
        Log.d(TAG, "Spotify logged in");
        PlayerLab playerLab = (PlayerLab) getApplicationContext();
        playerLab.setupSpotifyPlayer(mPlayer);
        setController();
        hideProcessDialog();
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

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }


        //TODO: Make it start on the profile fragment
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                //TODO: Missing Upload(2)
//                case 0:
//                    //timeline
//                    return TimelineFragment.newInstance();
                case 0:
                    //search
                    return SearchFragment.newInstance();
//                case 2:
//                    //upload
//
                case 1:
                    // chat
                    return ChatListFragment.newInstance();
                case 2:
                    //profile
                    return ProfileFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            Drawable image = getResources().getDrawable(imageResId[position]);
//            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//            SpannableString sb = new SpannableString("  ");
//            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//            sb.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return sb;
//        }

    }
}
