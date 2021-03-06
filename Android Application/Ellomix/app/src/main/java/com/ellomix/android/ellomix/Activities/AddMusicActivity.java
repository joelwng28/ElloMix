package com.ellomix.android.ellomix.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ellomix.android.ellomix.FirebaseAPI.FirebaseService;
import com.ellomix.android.ellomix.Model.Track;
import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.SoundCloudAPI.SCService;
import com.ellomix.android.ellomix.SoundCloudAPI.SoundCloudAPI;
import com.ellomix.android.ellomix.SoundCloudDataModel.SCTrack;
import com.ellomix.android.ellomix.SpotifyAPI.SPService;
import com.ellomix.android.ellomix.SpotifyAPI.SpotifyAPI;
import com.ellomix.android.ellomix.SpotifyAPI.SpotifyResponse;
import com.ellomix.android.ellomix.SpotifyDataModel.SPTrack;
import com.ellomix.android.ellomix.Style.SearchViewStyle;

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
    public static final String SEARCH_GO_BTN = "search_go_btn";

    private RecyclerView mSearchResultRecyclerView;
    private List<Track> mSoundcloudList;
    private List<Track> mSpotifyList;
    private List<Track> mYoutubeList;
    private List<Track> mTrackList;
    private boolean mSpotifyFlag;
    private boolean mSoundcloudFlag;
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

        mSpotifyList = new ArrayList<Track>();
        mSoundcloudList = new ArrayList<Track>();
        mYoutubeList = new ArrayList<Track>();
        mTrackList = new ArrayList<Track>();

        mTracksSelected = new HashSet<>();

        mChatId = getIntent().getStringExtra(EXTRA_CHATID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_music_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search...");
        //TODO: Apply Material Design to fit both SearchView and Action button in ActionBar

//        MenuItem addItem = menu.findItem(R.id.menu_item_add_music);
//        View addItemView = (View) addItem.getActionView();
//        int searchViewWidth = searchView.getMaxWidth() - addItemView.getWidth();
        searchView.setMaxWidth(1100);
//        searchView.setSubmitButtonEnabled(true);

        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.DKGRAY);
            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
            if (searchText != null) {
                searchText.setTextColor(Color.WHITE);
                searchText.setHintTextColor(Color.WHITE);
            }
        }

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        mTrackList = new ArrayList<Track>();

                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                        //TODO: Implement youtube
//                        youtubeSearchAPI.AsyncResponse asyncResponse =
//                                new youtubeSearchAPI.AsyncResponse() {
//                                    @Override
//                                    public void processFinish(List<Track> outputResult) {
//                                        mTrackList = outputResult;
//                                    }
//                                };
//
//                        new youtubeSearchAPI(asyncResponse, query);

                        //SpotifyAPI API service
                        SPService spService = SpotifyAPI.getService();

                        spService.searchFor(query).enqueue(new Callback<SpotifyResponse>() {
                            @Override
                            public void onResponse(Response<SpotifyResponse> response, Retrofit retrofit) {
                                if (response.isSuccess()) {
                                    List<SPTrack> tracks = response.body().getTracks().getItems();
                                    mSpotifyList = new ArrayList<Track>(tracks);
                                    mSpotifyFlag = true;
                                    buildList();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.d(TAG, "SpotifyAPI search failed");

                            }
                        });

                        // Soundcloud API service
                        SCService scService = SoundCloudAPI.getService();

                        //TODO: sanitize input ex<script><dghdfgkjhdf></scrpt>
                        scService.searchFor(query).enqueue(new Callback<List<SCTrack>>() {
                            @Override
                            public void onResponse(Response<List<SCTrack>> response, Retrofit retrofit) {
                                if (response.isSuccess()) {
                                    //TODO: sanitize display ex<script><dghdfgkjhdf></scrpt>
                                    List<SCTrack> tracks = response.body();
                                    mSoundcloudList = new ArrayList<Track>(tracks);
                                    mSoundcloudFlag = true;
                                    buildList();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.d(TAG, "Soundcloud search failed");
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

    public void buildList() {
        if (mSpotifyFlag && mSoundcloudFlag) {
            int spSize = mSpotifyList.size();
            int scSize = mSoundcloudList.size();
            int i = 0;
            int j = 0;
            while(i < spSize || j < scSize) {
                if (i < spSize) {
                    mTrackList.add(mSpotifyList.get(i));
                    i++;
                }
                if (j < scSize) {
                    mTrackList.add(mSoundcloudList.get(j));
                    j++;
                }
            }
            mSpotifyFlag = false;
            mSoundcloudFlag = false;
            updateUI();
        }
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
                String songsAdded = res.getQuantityString(R.plurals.numberOfSongsAdded, count, count);
                Toast.makeText(this, songsAdded, Toast.LENGTH_SHORT).show();
                finish();
//                Intent intent = GroupPlaylistActivity.newIntent(this, mChatId);
//                finish();
//                startActivity(intent);
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
        private TextView mTrackArtistTextView;
        private ImageView mTrackSourceImageView;
        private Track mTrack;
        private boolean isSelected;

        public SearchResultViewHolder(View view) {
            super(view);
            mTrackImageView = (ImageView) view.findViewById(R.id.track_image);
            mTrackTitleTextView = (TextView) view.findViewById(R.id.track_title);
            mTrackArtistTextView = (TextView) view.findViewById(R.id.track_artist);
            mTrackSourceImageView = (ImageView) view.findViewById(R.id.track_source);
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

//            Intent i = YTPlayerActivity.newIntent(AddMusicActivity.this, mTrack.getID());
//            startActivity(i);
        }

        //TODO: Implement long click listener so you can preview the song
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
