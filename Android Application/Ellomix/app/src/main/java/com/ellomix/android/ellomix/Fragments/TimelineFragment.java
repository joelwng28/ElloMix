package com.ellomix.android.ellomix.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.SoundCloudAPI.SCMusicService;
import com.ellomix.android.ellomix.SoundCloudDataModel.SCTrack;
import com.ellomix.android.ellomix.SoundCloudAPI.SCService;
import com.ellomix.android.ellomix.SoundCloudAPI.SoundCloud;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by abetorres on 11/16/16.
 */

public class TimelineFragment extends Fragment {

    private static final String TAG = "TimelineFragment";
    private List<SCTrack> mListItems = new ArrayList<>();
    private RecyclerView mTimelineRecyclerView;
    private String[] usersDemo = {"Abe Torres", "Neil Tanner", "Akshay", "Elena Carrasco", "Micah Peoples"};
    private String[] messagesDemo = {
            "DOPE",
            "Some sick beats, everyone check it ok",
            "New favourite song",
            "So mellow",
            "Chillax"
    };

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup list
        loadRecentTracks();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_time_line, container, false);

        mTimelineRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_time_line_recycler_view);
        mTimelineRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        mTimelineRecyclerView.setAdapter(new TimelineAdapter(mListItems));
    }

    private int randomNumberGenerator() {
        Random rand = new Random();
        int randomNum = rand.nextInt(4 + 1);
        return randomNum;
    }

    private void loadRecentTracks() {
        SCService scService = SoundCloud.getService();
        Call<List<SCTrack>> call = scService.getRecentTracks(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));

        call.enqueue(new Callback<List<SCTrack>>() {
            @Override
            public void onResponse(Response<List<SCTrack>> response, Retrofit retrofit) {
                mListItems = response.body();
                setupAdapter();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "Error: " + t.toString());
            }
        });
    }

    private class TimelineHolder extends RecyclerView.ViewHolder {

        SCTrack mSCTrack;
        private TextView mUploaderTextView;
        private TextView mLongAgoTextView;
        private ImageView mTrackArtworkImageView;
        private TextView mArtistTextView;
        private TextView mTitleTextView;
        private TextView mMessageTextView;


        public TimelineHolder(View itemView) {
            super(itemView);

            //bind resources
            mUploaderTextView = (TextView) itemView.findViewById(R.id.uploader_text_view);
            mLongAgoTextView = (TextView) itemView.findViewById(R.id.long_ago_text_view);
            mTrackArtworkImageView = (ImageView) itemView.findViewById(R.id.track_artwork_image_view);
            mArtistTextView = (TextView) itemView.findViewById(R.id.artist_text_view);
            mTitleTextView = (TextView) itemView.findViewById(R.id.song_title_text_view);
            mMessageTextView = (TextView) itemView.findViewById(R.id.message_text_view);
        }

        public void bindItem(SCTrack SCTrack) {
            mSCTrack = SCTrack;

            mUploaderTextView.setText(usersDemo[randomNumberGenerator()]);
            mLongAgoTextView.setText("0 sec");
            Picasso.with(getActivity())
                    .load(mSCTrack.getArtworkURL())
                    //.placeholder(R.drawable.art_work_placeholder)
                    .into(mTrackArtworkImageView);
            mTrackArtworkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "play button pressed");
                    Intent intent = SCMusicService.newIntent(getActivity(), mSCTrack.getStreamURL(), mSCTrack.getTitle());
                    getActivity().startService(intent);
                }
            });
            mArtistTextView.setText(SCTrack.getUser().getUserName());
            mTitleTextView.setText(SCTrack.getTitle());
            mMessageTextView.setText(messagesDemo[randomNumberGenerator()]);
        }
    }

    private class TimelineAdapter extends RecyclerView.Adapter<TimelineHolder> {

        private List<SCTrack> mSCTrackList;

        public TimelineAdapter(List<SCTrack> SCTracks) {
            mSCTrackList = SCTracks;
        }

        @Override
        public TimelineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.time_line_item, parent, false);
            return new TimelineHolder(view);
        }

        @Override
        public void onBindViewHolder(TimelineHolder holder, int position) {
            SCTrack SCTrack = mSCTrackList.get(position);
            holder.bindItem(SCTrack);
        }

        @Override
        public int getItemCount() {
            return mListItems.size();
        }
    }
}
