package com.ellomix.android.ellomix.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ellomix.android.ellomix.Activities.AddMusicActivity;
import com.ellomix.android.ellomix.Model.Track;
import com.ellomix.android.ellomix.R;

/**
 * Created by Akshay on 3/28/17.
 */

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private RecyclerView mSearchResultRecyclerView;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchResultRecyclerView = (RecyclerView) v.findViewById(R.id.search_result_recyler_view);
        mSearchResultRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

//    public void updateUI() {
//        if (mAdapter == null) {
//            mAdapter = new AddMusicActivity.SearchResultAdapter(mTrackList);
//            mSearchResultRecyclerView.setAdapter(mAdapter);
//        }
//        else {
//            mAdapter.setTracks(mTrackList);
//            mAdapter.notifyDataSetChanged();
//        }
//    }

//    private class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        private ImageView mTrackImageView;
//        private TextView mTrackTitleTextView;
//        private TextView mTrackArtistTextView;
//        private ImageView mTrackSourceImageView;
//        private Track mTrack;
//
//        public SearchResultViewHolder(View view) {
//            super(view);
//            mTrackImageView = (ImageView) view.findViewById(R.id.track_image);
//            mTrackTitleTextView = (TextView) view.findViewById(R.id.track_title);
//            mTrackArtistTextView = (TextView) view.findViewById(R.id.track_artist);
//            mTrackSourceImageView = (ImageView) view.findViewById(R.id.track_source);
//            view.setOnClickListener(this);
//        }
//
//        public void bindTrack(Track track) {
//            mTrack = track;
//            if (mTrack.getArtworkURL() != null) {
//                Glide.with(getActivity())
//                        .load(mTrack.getArtworkURL())
//                        .into(mTrackImageView);
//            }
//            mTrackTitleTextView.setText(mTrack.getTitle());
//            mTrackArtistTextView.setText(mTrack.getArtist());
//            int source = -1;
//            switch (mTrack.getSource()) {
//                case SPOTIFY:
//                    source = R.drawable.spotify;
//                    break;
//                case SOUNDCLOUD:
//                    source = R.drawable.soundcloud;
//                    break;
//                case YOUTUBE:
//                    source = R.drawable.youtube;
//                    break;
//                default:
//                    source = -1;
//            }
//            if (source != -1) {
//                mTrackSourceImageView.setImageResource(source);
//            }
//
//        }
//
//        @Override
//        public void onClick(View v) {
//
//        }
//
//        //TODO: Implement long click listener so you can preview the song
//    }
}
