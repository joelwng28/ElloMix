package com.ellomix.android.ellomix.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ellomix.android.ellomix.FirebaseAPI.FirebaseService;
import com.ellomix.android.ellomix.Model.Track;
import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.SoundCloudAPI.SCService;
import com.ellomix.android.ellomix.SoundCloudAPI.SoundCloud;
import com.ellomix.android.ellomix.SoundCloudDataModel.SCTrack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AddMusicActivity extends AppCompatActivity {

    private static final String TAG = "AddMusicActivity";
    private static final String EXTRA_CHATID = "chatId";

    private RecyclerView mSearchResultRecyclerView;
    private List<Track> mTrackList;
    private SearchResultAdapter mAdapter;
    private Set<Track> mTracksSelected;
    private String mChatId;

    public static Intent newIntent(Context context, String chatId) {
        Intent i = new Intent(context, AddMusicActivity.class);
        i.putExtra(EXTRA_CHATID, chatId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);
        mSearchResultRecyclerView = (RecyclerView)
                findViewById(R.id.search_result_recyler_view);
        mSearchResultRecyclerView.setLayoutManager(
                new LinearLayoutManager(getApplicationContext()));

        mTracksSelected = new HashSet<>();

        mChatId = getIntent().getStringExtra(EXTRA_CHATID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_music_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        SCService scService = SoundCloud.getService();

                        //TODO: sanitize display and input ex<script><dghdfgkjhdf></scrpt>
                        scService.searchFor(query).enqueue(new Callback<List<SCTrack>>() {
                            @Override
                            public void onResponse(Response<List<SCTrack>> response, Retrofit retrofit) {
                                if (response.isSuccess()) {
                                    //TODO: sanitize display and input ex<script><dghdfgkjhdf></scrpt>
                                    List<SCTrack> tracks = response.body();
                                    mTrackList = new ArrayList<Track>(tracks);
                                    updateUI();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(getApplicationContext(),
                                        "Search failed, try again",
                                        Toast.LENGTH_LONG).show();
                            }
                        });


                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_music:
                // Add tracks to firebase

                Iterator it = mTracksSelected.iterator();

                while (it.hasNext()) {
                    Track track = (Track) it.next();
                    FirebaseService.addMusic(mChatId, track);
                }
                Resources res = getResources();
                int count = mTracksSelected.size();
                String songsAdded = res.getQuantityString(R.plurals.numberOfSongsAdded, count);
                Toast.makeText(this, songsAdded, Toast.LENGTH_SHORT).show();
                Intent intent = GroupPlaylistActivity.newIntent(this, mChatId);
                finish();
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
        if (mAdapter == null) {
            mAdapter = new SearchResultAdapter(mTrackList);
            mSearchResultRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setTracks(mTrackList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mTrackImageView;
        private TextView mTrackTitleTextView;
        private Track mTrack;
        private boolean isSelected;

        public SearchResultViewHolder(View view) {
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
            if(!isSelected) {
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));

                mTracksSelected.add(mTrack);
            }
            else {
                v.setBackgroundColor(Color.TRANSPARENT);
                mTracksSelected.remove(mTrack);
            }
            isSelected = !isSelected;
        }
    }

    private class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {

        private List<Track> mTracks;

        public SearchResultAdapter(List<Track> tracks) {
            mTracks = tracks;
        }

        @Override
        public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View view = inflater.inflate(R.layout.track_list_item, parent, false);


            return new SearchResultViewHolder(view);
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
}
