package com.ellomix.android.ellomix.SoundCloudAPI;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;

/**
 * Created by teodor on 11.03.2016.
 */

// TODO: Need to figure how to use this code to create a notification media bar
public class SCMusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = "SCMusicService";
    public static boolean IS_SERVICE_RUNNING = false;
    private MediaPlayer player;
    private Notification notification;
    private String url, title;

    public static Intent newIntent(Context context, String songUrl, String songTitle) {
        Log.d(TAG, "SCMusicService Intent");
        Intent intent = new Intent(context, SCMusicService.class);
        intent.setAction("PLAY_FROM_APP");
        intent.putExtra("songUrl", songUrl);
        intent.putExtra("songTitle", songTitle);
        return intent;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "start of onStartCommand");
        if(intent != null)  {
            if (intent.getAction().equals("PLAY_FROM_APP") && intent.getExtras() != null) {
                Log.d(TAG, "data received");
                if (player != null) {
                    player.stop();
                    player.release();
                }
                player = new MediaPlayer();
                player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setOnPreparedListener(this);
                player.setOnCompletionListener(this);
                try {
                    Log.d(TAG, intent.getStringExtra("songUrl"));
                    url = intent.getExtras().getString("songUrl") + "?client_id=3e7f2924c47462bf79720ae5995194de";
                    title = intent.getExtras().getString("title");
                    player.setDataSource(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.prepareAsync();


            }

            else if(((String)intent.getAction()).equals("PLAY_FROM_NOTIF") && player != null) {
                player.start();
            }
            else if(((String)intent.getAction()).equals("PAUSE_FROM_NOTIF") && player != null) {
                player.pause();
            }
            else if(intent.getAction().equals("STOP_FROM_NOTIF") && player != null) {
                player.stop();
                player.release();
                player = null;
                stopForeground(true);
            }
        } else {
            Log.e(TAG, "INTENT extras null");
        }
        return START_STICKY;
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        //showNotification();
        Log.d(TAG, "onPrepared");
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp = null; // ori asta ori ii dau reset.
        stopForeground(true);
    }

//    public void showNotification() {
//        Intent notificationIntent = new Intent(this, SearchActivity.class);
//        notificationIntent.setAction("SEARCH_ACTIVITY");
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                notificationIntent, 0);
//
//        Intent playIntent = new Intent(this, SCMusicService.class);
//        playIntent.setAction("PLAY_FROM_NOTIF");
//        PendingIntent play = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Intent pauseIntent = new Intent(this, SCMusicService.class);
//        pauseIntent.setAction("PAUSE_FROM_NOTIF");
//        PendingIntent pause = PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        Intent stopIntent = new Intent(this, SCMusicService.class);
//        stopIntent.setAction("STOP_FROM_NOTIF");
//        PendingIntent stop = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        notification = new NotificationCompat.Builder(this)
//                .setContentTitle("SoundCloudAPI Player")
//                .setTicker("Soundcloud Player")
//                .setContentText(title)
//                .setSmallIcon(android.R.drawable.stat_notify_chat)
//                .setContentIntent(pendingIntent)
//                .setOngoing(true)
//                .addAction(android.R.drawable.ic_media_pause, "Pause", pause)
//                .addAction(android.R.drawable.ic_media_play, "Play", play)
//                .addAction(android.R.drawable.ic_media_next, "Stop", stop)
//                .build();
//        startForeground(354, notification);
//    }

}
