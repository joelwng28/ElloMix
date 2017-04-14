package com.ellomix.android.ellomix.Model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ellomix.android.ellomix.Activities.AddMusicActivity;
import com.ellomix.android.ellomix.R;

import java.util.List;

/**
 * Created by Akshay on 4/3/17.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
    private List<Track> mTracks;
    private Context mContext;

    public SearchResultAdapter(Context context, List<Track> tracks) {
        mTracks = tracks;
        mContext = context;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.track_list_item, parent, false);

        return new SearchResultViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
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
