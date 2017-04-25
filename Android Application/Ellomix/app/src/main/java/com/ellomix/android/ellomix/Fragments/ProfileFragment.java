package com.ellomix.android.ellomix.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.StreamAssetPathFetcher;
import com.ellomix.android.ellomix.Activities.LoginActivity;
import com.ellomix.android.ellomix.Activities.NewMessageActivity;
import com.ellomix.android.ellomix.FirebaseAPI.FirebaseService;
import com.ellomix.android.ellomix.Model.ChatLab;
import com.ellomix.android.ellomix.Model.FriendLab;
import com.ellomix.android.ellomix.Model.MusicLab;
import com.ellomix.android.ellomix.Model.Track;
import com.ellomix.android.ellomix.Model.User;
import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.Services.PlayerLab;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by abetorres on 12/17/16.
 */

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private CircleImageView mProfilePicImageView;
    private TextView mNumPostsTextView;
    private TextView mNumFollewerTextView;
    private TextView mNumFollowingTextView;
    private Button logOutButton;
    private RecyclerView mRecentMusicRecyclerView;
    private RecentMusicAdapter mAdapter;
    private PlayerLab mPlayerLab;

    //Firebase Instance variable
    private ChildEventListener mStatsEventListener;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mProfilePicImageView = (CircleImageView) v.findViewById(R.id.profile_picture_image_view);

        FirebaseUser firebaseUser = FirebaseService.getFirebaseUser();
        String userId = "";
        if (firebaseUser != null) {
            String photoURL = firebaseUser.getPhotoUrl().toString();
            userId = firebaseUser.getUid();

            if (photoURL != null) {
                Glide.with(getActivity())
                        .load(photoURL)
                        .into(mProfilePicImageView);
            }
        }

        mNumPostsTextView = (TextView) v.findViewById(R.id.posts_count_text_view);
        mNumFollewerTextView = (TextView) v.findViewById(R.id.followers_count_text_view);
        mNumFollowingTextView = (TextView) v.findViewById(R.id.following_count_text_view);
        mRecentMusicRecyclerView = (RecyclerView) v.findViewById(R.id.recent_music_recycler_view);
        mRecentMusicRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        mPlayerLab = (PlayerLab) getApplicationContext();


        mStatsEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // do nothing
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                User curUser = (User) dataSnapshot.getValue(User.class);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        if (userId.length() != 0) {
            FirebaseService.getUserQuery(userId)
                    .addChildEventListener(mStatsEventListener);
        }

        logOutButton = (Button) v.findViewById(R.id.log_out_button);
        logOutButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Cleaning up
                        ChatLab chatLab = ChatLab.get(getActivity());
                        chatLab.deleteDatabase();
                        FriendLab friendLab = FriendLab.get(getActivity());
                        friendLab.deleteDatabase();
                        mPlayerLab.terminateSpPlayer();
                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        getActivity().finish();
                        startActivity(i);
                    }
                }
        );
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.chat_feed_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_compose:
                Intent intent = new Intent(getContext(), NewMessageActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        MusicLab musicLab = MusicLab.get(getActivity());
        List<Track> tracks = musicLab.getTracks();
        Collections.sort(tracks, new Comparator<Track>() {
            @Override
            public int compare(Track track1, Track track2) {
                Date date1 = new Date(track1.getCreatedAt());
                Date date2 = new Date(track2.getCreatedAt());
                return date2.compareTo(date1);
            }
        });

        if (mAdapter == null) {
            mAdapter = new RecentMusicAdapter(tracks);
            mRecentMusicRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setTracks(tracks);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class RecentMusicViewHolder extends RecyclerView.ViewHolder {
        private Track mTrack;
        private CircleImageView mTrackImage;
        private TextView mTrackTitle;

        public RecentMusicViewHolder(View view) {
            super(view);
            mTrackImage = (CircleImageView) view.findViewById(R.id.recent_track_image);
            mTrackTitle = (TextView) view.findViewById(R.id.recent_track_title);
        }

        public void bindItem(Track track) {
            mTrack = track;
            if (mTrack.getArtworkURL() != null) {
                Glide.with(getApplicationContext())
                        .load(mTrack.getArtworkURL())
                        .into(mTrackImage);
            }
            mTrackTitle.setText(mTrack.getTitle());
        }

    }

    private class RecentMusicAdapter extends RecyclerView.Adapter<RecentMusicViewHolder> {

        private List<Track> mTracks;

        private RecentMusicAdapter(List<Track> tracks) {
            mTracks = tracks;
        }

        @Override
        public RecentMusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.recent_music_item, parent, false);

            return new RecentMusicViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecentMusicViewHolder holder, int position) {
            holder.bindItem(mTracks.get(position));
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
