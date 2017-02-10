package com.ellomix.android.ellomix.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import java.util.ArrayList;
import android.content.ContentUris;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.app.Notification;
import android.app.PendingIntent;
import android.view.View;
import android.widget.Toast;

import com.ellomix.android.ellomix.Model.MusicController;
import com.ellomix.android.ellomix.Model.Track;


/**
 * Created by AbelardoJose on 5/23/2016.
 */
public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private MediaPlayer mPlayer;
    private List<Track> mSongs;
    private int mSongPosn = 0;
    private final IBinder musicBind = new MusicBinder();
    private final static String TAG = "MusicService";
    private boolean shuffle = false;
    private Random rand;
    MusicController mServiceController;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener afChangeListener;

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mPlayer.stop();
        mPlayer.release();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSongPosn = 0;
        mPlayer = new MediaPlayer();
        initMusicPlayer();
        rand = new Random();
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

    @Override
    public void onDestroy() {
        stopForeground(true);
        audioManager.abandonAudioFocus(afChangeListener);
    }

    public void initMusicPlayer() {
        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
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

    public void setShuffle() {
        shuffle = !shuffle;
    }

    public void playSong (int position) {
        setSong(position);
        playSong();
    }

    public void playSong() {
        // play a song
        mPlayer.reset();
        // get the song
        Track playSong = mSongs.get(mSongPosn);
        //get the playable url
        String url = playSong.getStreamURL() + "?client_id=3e7f2924c47462bf79720ae5995194de";

        try {
            mPlayer.setDataSource(url);
        }
        catch (Exception e) {
            Log.e(TAG, "Error setting data source", e);
        }

        mPlayer.prepareAsync();
    }

    public void setList(List<Track> theSongs) {
        mSongs = theSongs;
    }

    public void setSong (int songIndex) {
        mSongPosn = songIndex;
    }

    public Track getCurrentSong() {
        return mSongs.get(mSongPosn);
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mPlayer.getCurrentPosition() == 0) {
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
        mp.start();
        mServiceController.show(5*1000);
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public int getPosn(){
        return mPlayer.getCurrentPosition();
    }

    public int getDur(){
        return mPlayer.getDuration();
    }

    public boolean isPng(){
        return mPlayer.isPlaying();
    }

    public void pausePlayer(){
        mPlayer.pause();
    }

    public void seek(int posn){
        mPlayer.seekTo(posn);
    }

    public void go(){
        mPlayer.start();
    }

    public void playPrev() {
        mSongPosn--;
        if (mSongPosn < 0) {
            mSongPosn = mSongs.size() - 1;
        }
        playSong();
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
        if (mSongPosn >= mSongs.size()) {
            mSongPosn = 0;
        }

        playSong();
    }

}
