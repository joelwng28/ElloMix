package com.ellomix.android.ellomix.Activities;

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
import com.ellomix.android.ellomix.Style.CustomViewPager;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.SpotifyPlayer;
import android.widget.MediaController.MediaPlayerControl;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by abetorres on 12/10/16.
 */

public class ScreenSlidePagerActivity extends AppCompatActivity implements MediaPlayerControl {

    private static final String TAG = "ScreenSlActivity";
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
    private MusicLab mMusicLab;
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

        mPlaylist = new ArrayList<>();
        //setup Media controller
        setController();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    private void setController() {
        if (mController == null) {
            mController = new MusicController(this, false);
        }
        mController.setMediaPlayer(this);
        mController.setAnchorView(mPager);
        mController.setEnabled(true);
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected (ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setList(mPlaylist);
            musicService.setController(mController);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public void setTrack(Track track) {
        mCurrentTrack = track;
        switch(mCurrentTrack.getSource()) {
            case SPOTIFY:
                PlayerLab playerLab = (PlayerLab) getApplicationContext();
                mPlayer = playerLab.getPlayer();
                //TODO: Sometimes get a null pointer exception on mPlayer
                if (mPlayer != null) {
                    mPlayer.playUri(null, mCurrentTrack.getStreamURL(), 0, 0);
                }
                break;
            case SOUNDCLOUD:
                Log.i(TAG, "received soundcloud song");
                if (mPlaylist.size() == 0) {
                    mPlaylist.add(mCurrentTrack);
                }
                else {
                    mPlaylist.set(0, mCurrentTrack);
                }
                musicService.setList(mPlaylist);
                musicService.playSong(0);
                break;
            case YOUTUBE:

                break;
        }
        mController.show(0);
    }

    public void playTrack() {
        switch(mCurrentTrack.getSource()) {
            case SPOTIFY:
                mPlayer.resume(null);
                break;
            case SOUNDCLOUD:
                musicService.go();
                break;
            case YOUTUBE:

                break;
        }
    }

    public void pauseTrack() {
        if (mPlayer != null) {
            mPlayer.pause(null);
        }
        if (musicService.isPng()) {
            musicService.pausePlayer();
        }
//        switch(mCurrentTrack.getSource()) {
//            case SPOTIFY:
//                mPlayer.pause(null);
//                break;
//            case SOUNDCLOUD:
//                musicService.pausePlayer();
//                break;
//            case YOUTUBE:
//
//                break;
//        }
    }

    private int getTrackDuration() {
        switch(mCurrentTrack.getSource()) {
            case SPOTIFY:
                mPlaybackState = mPlayer.getPlaybackState();
                if (mPlaybackState != null) {
                    //TODO: Figure how to get duration of spotify song
                    return 0;
                }
                else {
                    return 0;
                }
            case SOUNDCLOUD:
                //TODO: Need to get duration once the music has loaded completely
                if(musicService != null && musicBound){
                    return 0; //musicService.getDur();
                }
                else {
                    return 0;
                }
            case YOUTUBE:
                return 0;

            default:
                return 0;
        }
    }

    private int getTrackCurrentPosition() {
        switch(mCurrentTrack.getSource()) {
            case SPOTIFY:
                mPlaybackState = mPlayer.getPlaybackState();
                if (mPlaybackState != null) {
                    return 0;
                }
                else {
                    return 0;
                }
            case SOUNDCLOUD:
                if (musicService != null && musicBound) {
                    return 0; //musicService.getPosn();
                }
                else {
                    return 0;
                }
            case YOUTUBE:
                return 0;

            default:
                return 0;
        }
    }

    private void seekTrackTo(int pos) {
        switch(mCurrentTrack.getSource()) {
            case SPOTIFY:
                if (mPlayer != null) {
                    mPlayer.seekToPosition(null, pos);
                }
                break;
            case SOUNDCLOUD:
                if (musicService != null && musicBound) {
                    musicService.seek(pos);
                }
                break;
            case YOUTUBE:

                break;
            default:
        }
    }

    private boolean isTrackPlaying() {
        switch(mCurrentTrack.getSource()) {
            case SPOTIFY:
                mPlaybackState = mPlayer.getPlaybackState();
                if (mPlaybackState != null) {
                    return mPlaybackState.isPlaying;
                }
                else {
                    return false;
                }
            case SOUNDCLOUD:
                if (musicService != null && musicBound) {
                    return musicService.isPng();
                }
                else {
                    return false;
                }
            case YOUTUBE:
                return false;
            default:
                return false;
        }
    }



    @Override
    public void start() {
        playTrack();
    }

    @Override
    public void pause() {
        pauseTrack();
    }

    @Override
    public int getDuration() {
        return getTrackDuration();
    }

    @Override
    public int getCurrentPosition() {
        return getTrackCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        seekTrackTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return isTrackPlaying();
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
