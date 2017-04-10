package com.ellomix.android.ellomix.Model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ellomix.android.ellomix.R;

/**
 * Created by Akshay on 4/3/17.
 */

public class SearchResultViewHolder extends RecyclerView.ViewHolder {

    //TODO: Implement click listener

    private ImageView mTrackImageView;
    private TextView mTrackTitleTextView;
    private TextView mTrackArtistTextView;
    private ImageView mTrackSourceImageView;
    private Track mTrack;
    private Context mContext;

    public SearchResultViewHolder(View view, Context context) {
        super(view);
        mContext = context;
        mTrackImageView = (ImageView) view.findViewById(R.id.track_image);
        mTrackTitleTextView = (TextView) view.findViewById(R.id.track_title);
        mTrackArtistTextView = (TextView) view.findViewById(R.id.track_artist);
        mTrackSourceImageView = (ImageView) view.findViewById(R.id.track_source);
    }

    public void bindTrack(Track track) {
        mTrack = track;
        if (mTrack.getArtworkURL() != null) {
            Glide.with(mContext)
                    .load(mTrack.getArtworkURL())
                    .into(mTrackImageView);
        }
        mTrackTitleTextView.setText(mTrack.getTitle());
        mTrackArtistTextView.setText(mTrack.getArtist());
        int source = -1;
        switch (mTrack.getSource()) {
            case SPOTIFY:
                source = R.drawable.spotify;
                break;
            case SOUNDCLOUD:
                source = R.drawable.soundcloud;
                break;
            case YOUTUBE:
                source = R.drawable.youtube;
                break;
            default:
                source = -1;
        }
        if (source != -1) {
            mTrackSourceImageView.setImageResource(source);
        }
    }
}
