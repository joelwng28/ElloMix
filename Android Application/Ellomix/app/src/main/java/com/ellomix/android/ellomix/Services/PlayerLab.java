package com.ellomix.android.ellomix.Services;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;

import com.ellomix.android.ellomix.Model.MusicController;
import com.ellomix.android.ellomix.Model.Track;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay on 4/10/17.
 */

public class PlayerLab extends Application implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = "PlayerLab";

    private static PlayerLab singleton;

    private static final String STATUS = "ConnectedStatus";
    private static final String SpotifyStatus = "Spotify";
    private SharedPreferences mConnectedServices;

    // SpotifyAPI instance variable
    private SpotifyPlayer mSpotifyPlayer;

    // Soundcloud MediaPlayer instance variable
    private MediaPlayer mSoundcloudPlayer;
    private List<Track> mPlaylist;
    private int mSongPosn = 0;
    private boolean isSCPlayerPrepared;
    MusicController mServiceController;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener afChangeListener;
    private Track mCurrentTrack;
    private PlaybackState mPlaybackState;

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
    public void setupSpotifyPlayer(SpotifyPlayer player) {
        if (player != null) {
            mSpotifyPlayer = player;
            updateSpotifyStatus(true);
        }
    }
    public boolean isSpotifyLoggedIn() {
        return mSpotifyPlayer != null;
    }

    public void spotifyLogOut() {
        Spotify.destroyPlayer(this);
        mSpotifyPlayer = null;
    }

    public SpotifyPlayer getPlayer() {
        return mSpotifyPlayer;
    }

    private void playSPSong() {
        if (mSpotifyPlayer != null) {
            mSpotifyPlayer.playUri(new Player.OperationCallback() {
                @Override
                public void onSuccess() {
                    mServiceController.show(0);
                }

                @Override
                public void onError(Error error) {
                    Log.e(TAG, error.toString());

                }
            }, mCurrentTrack.getStreamURL(), 0, 0);
        }
    }
    //General playback methods

    public void setList(List<Track> theSongs) {
        mPlaylist = theSongs;
    }

    public void setSong(int songIndex) {
        mSongPosn = songIndex;
    }

    public Track getCurrentSong() {
        return mPlaylist.get(mSongPosn);
    }

    public void setOneTrack(Track track) {
        mPlaylist = new ArrayList<Track>();
        mPlaylist.add(track);

        setSong(0);
        mCurrentTrack = getCurrentSong();
        playTrack();
    }

    public void playTrack() {
        pauseTrack();
        mCurrentTrack = getCurrentSong();
        switch(mCurrentTrack.getSource()) {
            case SPOTIFY:
                playSPSong();
                break;
            case SOUNDCLOUD:
                playSCSong();
                break;
            case YOUTUBE:

                break;
        }
    }

    public void playTrack(int pos) {
        setSong(pos);
        playTrack();
    }

    public void pauseTrack() {
        if (mSpotifyPlayer != null) {
            mSpotifyPlayer.pause(null);
        }
        if (isSCPlayerPrepared && mSoundcloudPlayer.isPlaying()) {
            mSoundcloudPlayer.pause();
        }
    }

    public void playPrev() {
        mSongPosn--;
        if (mSongPosn < 0) {
            mSongPosn = mPlaylist.size() - 1;
        }
        playTrack();
    }

    //skip to next
    public void playNext(){
//        if (shuffle) {
//            int newSong = mSongPosn;
//            while (newSong == mSongPosn) {
//                newSong = rand.nextInt(mSongs.size());
//            }
//            mSongPosn = newSong;
//        }
        mSongPosn++;
        if (mSongPosn >= mPlaylist.size()) {
            mSongPosn = 0;
        }

        playTrack();
    }

    public int getTrackDuration() {
        switch(mCurrentTrack.getSource()) {
            case SPOTIFY:
                if (isSpotifyLoggedIn()) {
                    mPlaybackState = mSpotifyPlayer.getPlaybackState();
                }
                if (mPlaybackState != null) {
                    //TODO: Figure how to get duration of spotify song
                    return 0;
                }
                else {
                    return 0;
                }
            case SOUNDCLOUD:
                if (isSCPlayerPrepared) {
                    return mSoundcloudPlayer.getDuration();
                }
                else {
                    return 0;
                }
            case YOUTUBE:

            default:
                return 0;
        }
    }

    public int getTrackCurrentPosition() {
        switch(mCurrentTrack.getSource()) {
            case SPOTIFY:
                if (isSpotifyLoggedIn()) {
                    mPlaybackState = mSpotifyPlayer.getPlaybackState();
                }
                if (mPlaybackState != null) {
                    return (int) mPlaybackState.positionMs;
                }
                else {
                    return 0;
                }
            case SOUNDCLOUD:
                return mSoundcloudPlayer.getCurrentPosition();
            case YOUTUBE:

            default:
                return 0;
        }
    }

    public void seekTrackTo(int pos) {
        Track currentTrack = getCurrentSong();
        switch(mCurrentTrack.getSource()) {
            case SPOTIFY:
                if (mSpotifyPlayer != null) {
                    mSpotifyPlayer.seekToPosition(null, pos);
                }
                break;
            case SOUNDCLOUD:
                if (isSCPlayerPrepared) {
                    mSoundcloudPlayer.seekTo(pos);
                }
                break;
            case YOUTUBE:

                break;
            default:
        }
    }

    public boolean isTrackPlaying() {
        switch(mCurrentTrack.getSource()) {
            case SPOTIFY:
                mPlaybackState = mSpotifyPlayer.getPlaybackState();
                if (mPlaybackState != null) {
                    return mPlaybackState.isPlaying;
                }
                else {
                    return false;
                }
            case SOUNDCLOUD:
                return mSoundcloudPlayer.isPlaying();
            case YOUTUBE:
                return false;
            default:
                return false;
        }
    }

    //Soundcloud MediaPlayer callback

    public void createMediaPlayer() {
        mSongPosn = 0;
        mSoundcloudPlayer = new MediaPlayer();
        initMusicPlayer();
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        //        final Runnable mDelayedStopRunnable = new Runnable() {
//            @Override
//            public void run() {
//                mPlayer.stop();
//            }
//        };
//
//        final Handler mHandler = new Handler();
//        afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
//            @Override
//            public void onAudioFocusChange(int focusChange) {
//                switch (focusChange) {
//                    case AudioManager.AUDIOFOCUS_GAIN:
//                        // Your app has been granted audio focus again
//                        // Raise volume to normal, restart playback if necessary
//                        if (mPlayer != null) {
//                            playSong();
//                        }
//                        break;
//                    case AudioManager.AUDIOFOCUS_LOSS:
//                        // Permanent loss of audio focus
//                        // Pause playback immediately
//                        mPlayer.pause();
//                        mHandler.postDelayed(mDelayedStopRunnable, 30);
//                        break;
//
//                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                        mPlayer.pause();
//                        break;
//                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
//                        // Lower the volume, keep playing
//                        break;
//
//                }
//            }
//        };
//
//        // Request audio focus for playback
//        int result = audioManager.requestAudioFocus(afChangeListener,
//                // Use the music stream.
//                AudioManager.STREAM_MUSIC,
//                // Request permanent focus.
//                AudioManager.AUDIOFOCUS_GAIN);
//
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            // Start playback
//              playSong();
//
//        }
    }

    public void initMusicPlayer() {
        mSoundcloudPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mSoundcloudPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mSoundcloudPlayer.setOnPreparedListener(this);
        mSoundcloudPlayer.setOnCompletionListener(this);
        mSoundcloudPlayer.setOnErrorListener(this);
    }

    public void setController(MusicController mc) {
        mServiceController = mc;
        mServiceController.setPrevNextListeners(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playNext();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPrev();
                    }
                }
        );
    }

    public void anchorControllerView(View v) {
        mServiceController.setAnchorView(v);
    }

    public void playSCSong() {
        Log.i(TAG, "playing soundcloud song");
        // play a song
        mSoundcloudPlayer.reset();
        isSCPlayerPrepared = false;
        // get the song
        Track playSong = getCurrentSong();
        //get the playable url
        String url = playSong.getStreamURL() + "?client_id=3e7f2924c47462bf79720ae5995194de";

        try {
            mSoundcloudPlayer.setDataSource(url);
        }
        catch (Exception e) {
            Log.e(TAG, "Error setting data source", e);
        }

        mSoundcloudPlayer.prepareAsync();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mSoundcloudPlayer.getCurrentPosition() == 0) {
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isSCPlayerPrepared = true;
        mp.start();
        mServiceController.show(0);
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
