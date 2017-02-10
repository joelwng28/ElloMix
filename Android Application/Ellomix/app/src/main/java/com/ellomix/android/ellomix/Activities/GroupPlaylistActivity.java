package com.ellomix.android.ellomix.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ellomix.android.ellomix.FirebaseAPI.FirebaseService;
import com.ellomix.android.ellomix.Model.MusicController;
import com.ellomix.android.ellomix.Model.Track;
import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.Services.MusicService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import com.ellomix.android.ellomix.Services.MusicService.MusicBinder;
import android.widget.MediaController.MediaPlayerControl;

public class GroupPlaylistActivity extends AppCompatActivity implements MediaPlayerControl {

    private static final String TAG = "GroupPlaylistActivity";
    private static final String EXTRA_CHATID = "chatId";

    private RecyclerView mGroupPlaylistRecyclerView;
    private PlaylistAdapter mAdapter;
    private List<Track> mGroupPlaylist;
    private String mChatId;
    private Intent playIntent;
    private MusicService musicService;
    private boolean musicBound = false;
    private MusicController mController;

    //Firebase instance variable
    ChildEventListener playlistEventListener;

    public static Intent newIntent(Context context, String chatId) {
        Intent i = new Intent(context, GroupPlaylistActivity.class);
        i.putExtra(EXTRA_CHATID, chatId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_playlist);

        mGroupPlaylistRecyclerView = (RecyclerView)
                findViewById(R.id.group_playlist_recycler_view);
        mGroupPlaylistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mChatId = getIntent().getStringExtra(EXTRA_CHATID);

        mGroupPlaylist = new ArrayList<>();

        playlistEventListener =
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Track newTrack = (Track) dataSnapshot.getValue(Track.class);
                        mGroupPlaylist.add(newTrack);
                        updateUI();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        // If song is removed from playlist
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };

        FirebaseService
                .getChatPlaylistQuery(mChatId)
                .addChildEventListener(playlistEventListener);
        setController();

    }

    @Override
    protected void onStart() {
        super.onStart();
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
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(playIntent);
        FirebaseService
                .getChatPlaylistQuery(mChatId)
                .removeEventListener(playlistEventListener);
        musicService = null;
    }

    private void setController() {
        if (mController == null) {
            mController = new MusicController(this);
        }
        mController.setMediaPlayer(this);
        mController.setAnchorView(findViewById(R.id.layout_group_playlist));
        mController.setEnabled(true);
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected (ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder) service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setList(mGroupPlaylist);
            musicService.setController(mController);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public void updateUI() {
        if (mAdapter == null) {
            mAdapter = new PlaylistAdapter(mGroupPlaylist);
            mGroupPlaylistRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setTracks(mGroupPlaylist);
            if (musicService != null) {
                musicService.setList(mGroupPlaylist);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void start() {
        musicService.go();
    }

    @Override
    public void pause() {
        musicService.pausePlayer();
    }

    @Override
    public int getDuration() {
        if(musicService != null && musicBound && musicService.isPng()){
            return musicService.getDur();
        }
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicService != null && musicBound && musicService.isPng()){
            return musicService.getPosn();
        }
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicService.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicService != null && musicBound) {
            return musicService.isPng();
        }
        return false;
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

    private class PlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mTrackImageView;
        private TextView mTrackTitleTextView;
        private Track mTrack;
        private int mPosition;

        public PlaylistViewHolder(View view) {
            super(view);
            mTrackImageView = (ImageView) view.findViewById(R.id.track_image);
            mTrackTitleTextView = (TextView) view.findViewById(R.id.track_title);
            view.setOnClickListener(this);
        }

        public void setPosition(int position) {
            mPosition = position;
        }

        public void bindTrack(Track track) {
            mTrack = track;
            if (mTrack.getArtworkURL() != null) {
                Glide.with(getApplicationContext())
                        .load(mTrack.getArtworkURL())
                        .into(mTrackImageView);
            }
            mTrackTitleTextView.setText(mTrack.getTitle());
        }

        @Override
        public void onClick(View v) {
            musicService.playSong(mPosition);
        }
    }

    private class PlaylistAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {

        private List<Track> mTracks;

        public PlaylistAdapter(List<Track> tracks) {
            mTracks = tracks;
        }

        @Override
        public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View view = inflater.inflate(R.layout.track_list_item, parent, false);

            return new PlaylistViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PlaylistViewHolder holder, int position) {
            holder.setPosition(position);
            holder.bindTrack(mTracks.get(position));
        }

        @Override
        public int getItemCount() {
            return mTracks.size();
        }

        public void setTracks(List<Track> tracks) {
            mTracks = tracks;
        }
    }
}
