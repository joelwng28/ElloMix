package com.ellomix.android.ellomix.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.ellomix.android.ellomix.Model.Track;
import com.ellomix.android.ellomix.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class GroupPlaylistActivity extends AppCompatActivity {

    private static final String TAG = "GroupPlaylistActivity";
    private static final String EXTRA_CHATID = "chatId";

    private RecyclerView mGroupPlaylistRecyclerView;
    private PlaylistAdapter mAdapter;
    private List<Track> mGroupPlaylist;
    private String mChatId;

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
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseService
                .getChatPlaylistQuery(mChatId)
                .removeEventListener(playlistEventListener);
    }

    public void updateUI() {
        if (mAdapter == null) {
            mAdapter = new PlaylistAdapter(mGroupPlaylist);
            mGroupPlaylistRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setTracks(mGroupPlaylist);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class PlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mTrackImageView;
        private TextView mTrackTitleTextView;
        private Track mTrack;

        public PlaylistViewHolder(View view) {
            super(view);
            mTrackImageView = (ImageView) view.findViewById(R.id.track_image);
            mTrackTitleTextView = (TextView) view.findViewById(R.id.track_title);
            view.setOnClickListener(this);
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
