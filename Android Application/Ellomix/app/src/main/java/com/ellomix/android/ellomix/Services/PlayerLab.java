package com.ellomix.android.ellomix.Services;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;

import com.ellomix.android.ellomix.Activities.ScreenSlidePagerActivity;
import com.ellomix.android.ellomix.FirebaseAPI.FirebaseService;
import com.ellomix.android.ellomix.Messaging.Chat;
import com.ellomix.android.ellomix.Model.ChatLab;
import com.ellomix.android.ellomix.Model.FriendLab;
import com.ellomix.android.ellomix.Model.MusicController;
import com.ellomix.android.ellomix.Model.MusicLab;
import com.ellomix.android.ellomix.Model.Track;
import com.ellomix.android.ellomix.Model.User;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Akshay on 4/10/17.
 */

public class PlayerLab extends Application implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, SpotifyPlayer.NotificationCallback {

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

    private User mCurrentUser;

    public interface FirebasePresenter {
        void finishLoading();
    }

    public static PlayerLab getInstance(){
        return singleton;
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
        super.onTerminate();
        terminateSpPlayer();
    }

    public void userLogIn(User user) {
        mCurrentUser = user;
    }

    public void userLogOut() {
//        mCurrentUser = null;
        ChatLab chatLab = ChatLab.get(getApplicationContext());
        chatLab.deleteDatabase();
        FriendLab friendLab = FriendLab.get(getApplicationContext());
        friendLab.deleteDatabase();
        MusicLab musicLab = MusicLab.get(getApplicationContext());
        musicLab.deleteDatabase();
        terminateSpPlayer();
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    //Check connected status

    public void terminateSpPlayer() {
        pauseTrack();
        Spotify.destroyPlayer(this);
        updateSpotifyStatus(false);
        mSpotifyPlayer = null;
    }

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
            Log.i(TAG, "Spotify Setup");
            mSpotifyPlayer = player;
            updateSpotifyStatus(true);
            mSpotifyPlayer.addNotificationCallback(this);
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
        if (isSpotifyLoggedIn()) {
            mSpotifyPlayer.playUri(new Player.OperationCallback() {
                @Override
                public void onSuccess() {
                    //nothing
                    Log.i(TAG, "Play Spotify");
                }

                @Override
                public void onError(Error error) {
                    Log.e(TAG, error.toString());

                }
            }, mCurrentTrack.getStreamURL(), 0, 0);
        }
    }

    private void resumeSPSong() {
        if (isSpotifyLoggedIn()) {
            mSpotifyPlayer.resume(new Player.OperationCallback() {
                @Override
                public void onSuccess() {
                    //nothing
                    Log.i(TAG, "Resume Spotify");
                }

                @Override
                public void onError(Error error) {
                    Log.e(TAG, error.toString());

                }
            });
        }
    }

    // SpotifyAPI player NotificationCallback

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d(TAG, "Playback event received: " + playerEvent.name());
        //mPlaybackState = mSpotifyPlayer.getPlaybackState();
        switch (playerEvent) {
            // Handle event type as necessary
            case kSpPlaybackNotifyPlay:
                mServiceController.show(0);
                break;
            case kSpPlaybackNotifyPause:
                mServiceController.show(0);
                break;
            case kSpPlaybackNotifyTrackChanged:
                break;
            case kSpPlaybackNotifyNext:
                break;
            case kSpPlaybackNotifyPrev:
                break;
            case kSpPlaybackNotifyShuffleOn:
                break;
            case kSpPlaybackNotifyShuffleOff:
                break;
            case kSpPlaybackNotifyRepeatOn:
                break;
            case kSpPlaybackNotifyRepeatOff:
                break;
            case kSpPlaybackNotifyBecameActive:
                break;
            case kSpPlaybackNotifyBecameInactive:
                break;
            case kSpPlaybackNotifyLostPermission:
                break;
            case kSpPlaybackEventAudioFlush:
                break;
            case kSpPlaybackNotifyAudioDeliveryDone:
                break;
            case kSpPlaybackNotifyContextChanged:
                break;
            case kSpPlaybackNotifyTrackDelivered:
                break;
            case kSpPlaybackNotifyMetadataChanged:
                break;
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
        if (mPlaylist.size() > 0) {
            mCurrentTrack = getCurrentSong();
        }
        if (mCurrentTrack != null) {
            switch (mCurrentTrack.getSource()) {
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
    }

    public void resumeTrack() {
        if (mCurrentTrack != null) {
            switch (mCurrentTrack.getSource()) {
                case SPOTIFY:
                    resumeSPSong();
                    break;
                case SOUNDCLOUD:
                    resumeSCSong();
                    break;
                case YOUTUBE:

                    break;
            }
        }
    }

    public void playTrack(int pos) {
        setSong(pos);
        playTrack();
    }

    public void pauseTrack() {
        if (isSpotifyLoggedIn()) {
            mSpotifyPlayer.pause(null);
            Log.i(TAG, "Spotify Paused");
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
        if (mCurrentTrack != null) {
            switch (mCurrentTrack.getSource()) {
                case SPOTIFY:
                    if (isSpotifyLoggedIn()) {
                        //mPlaybackState = mSpotifyPlayer.getPlaybackState();
                        if (mPlaybackState != null) {
                            //TODO: Figure how to get duration of spotify song
                            return 0;
                        }
                    }
                    return 0;
                case SOUNDCLOUD:
                    if (isSCPlayerPrepared) {
                        return mSoundcloudPlayer.getDuration();
                    } else {
                        return 0;
                    }
                case YOUTUBE:

                default:
                    return 0;
            }
        }
        else {
            return 0;
        }
    }

    public int getTrackCurrentPosition() {
        if (mCurrentTrack != null) {
            switch (mCurrentTrack.getSource()) {
                case SPOTIFY:
                    if (isSpotifyLoggedIn()) {
                        //mPlaybackState = mSpotifyPlayer.getPlaybackState();
                        if (mPlaybackState != null) {
                            return (int) mPlaybackState.positionMs;
                        }
                    }
                    return 0;
                case SOUNDCLOUD:
                    return mSoundcloudPlayer.getCurrentPosition();
                case YOUTUBE:

                default:
                    return 0;
            }
        }
        else {
            return 0;
        }
    }

    public void seekTrackTo(int pos) {
        if (mCurrentTrack != null) {
            switch (mCurrentTrack.getSource()) {
                case SPOTIFY:
                    if (isSpotifyLoggedIn()) {
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
    }

    public boolean isTrackPlaying() {
        if (mCurrentTrack != null) {
            switch (mCurrentTrack.getSource()) {
                case SPOTIFY:
                    if (isSpotifyLoggedIn()) {
                        //mPlaybackState = mSpotifyPlayer.getPlaybackState();
                        if (mPlaybackState != null) {
                            return mPlaybackState.isPlaying;
                        }
                    }
                    return false;
                case SOUNDCLOUD:
                    return mSoundcloudPlayer.isPlaying();
                case YOUTUBE:
                    return false;
                default:
                    return false;
            }
        }
        else {
            return false;
        }
    }

    //Soundcloud MediaPlayer callback

    public void createMediaPlayer() {
        mSongPosn = 0;
        mSoundcloudPlayer = new MediaPlayer();
        mPlaylist = new ArrayList<>();
        initMusicPlayer();
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);


        afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        // Your app has been granted audio focus again
                        // Raise volume to normal, restart playback if necessary
                        if (isTrackPlaying()) {
                            playTrack();
                        }
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        // Permanent loss of audio focus
                        // Pause playback immediately
                        pauseTrack();
                        break;

                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        pauseTrack();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        // Lower the volume, keep playing
                        break;

                }
            }
        };

        // Request audio focus for playback
        int result = audioManager.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Start playback
              playTrack();

        }
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

    private void playSCSong() {
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

    private void resumeSCSong() {
        mSoundcloudPlayer.start();
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

    //Firebase methods

    public void returningUserSetup(final FirebasePresenter presenter) {
        // TODO: Fix this
        boolean areFriendsLoaded = false;
        boolean areChatsLoaded = false;
        //Download friends from firebase
        final FriendLab friendLab = FriendLab.get(singleton);
        FirebaseService.getMainUserFollowingQuery().addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null) {
                            presenter.finishLoading();
                            return;
                        }
                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            // Get the userId and with that search for the user in firebase
                            String friendId = child.getKey();

                            FirebaseService.getUserQuery(friendId).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User friend = (User) dataSnapshot.getValue(User.class);
                                            if (friend != null) {
                                                friendLab.addFriend(friend);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                        }
                        presenter.finishLoading();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
