package com.ellomix.android.ellomix.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.bumptech.glide.Glide;
import com.ellomix.android.ellomix.Model.MusicLab;
import com.ellomix.android.ellomix.Model.Track;
import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.Services.PlayerLab;
import com.ellomix.android.ellomix.SoundCloudAPI.SCService;
import com.ellomix.android.ellomix.SoundCloudAPI.SoundCloudAPI;
import com.ellomix.android.ellomix.SoundCloudDataModel.SCTrack;
import com.ellomix.android.ellomix.SpotifyAPI.SPService;
import com.ellomix.android.ellomix.SpotifyAPI.SpotifyAPI;
import com.ellomix.android.ellomix.SpotifyAPI.SpotifyResponse;
import com.ellomix.android.ellomix.SpotifyDataModel.SPTrack;
import com.ellomix.android.ellomix.YoutubeAPI.youtubeSearchAPI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Akshay on 3/28/17.
 */

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private RecyclerView mSearchResultRecyclerView;
    private SearchResultAdapter mAdapter;
    private List<Track> mSoundcloudList;
    private List<Track> mSpotifyList;
    private List<Track> mYoutubeList;
    private List<Track> mTrackList;
    private PlayerLab mPlayerLab;
    private boolean mSpotifyCompleteFlag = false;
    private boolean mSoundcloudCompleteFlag = false;
    private boolean isPlayerSetup = false;
    private MusicLab mMusicLab;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSpotifyList = new ArrayList<Track>();
        mSoundcloudList = new ArrayList<Track>();
        mYoutubeList = new ArrayList<Track>();
        mTrackList = new ArrayList<Track>();
        isPlayerSetup = false;

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchResultRecyclerView = (RecyclerView) v.findViewById(R.id.search_result_recyler_view);
        mSearchResultRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPlayerLab = (PlayerLab) getApplicationContext();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search...");
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

                        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                        mTrackList = new ArrayList<Track>();

//                        new youtubeSearchAPI(new youtubeSearchAPI.AsyncResponse() {
//                            @Override
//                            public void processFinish(List<Track> outputResult) {
//                                mYoutubeList = outputResult;
//                            }
//                        }, query);

                        //SpotifyAPI API service
                        if (mPlayerLab.isSpotifyLoggedIn()) {
                            SPService spService = SpotifyAPI.getService();

                            spService.searchFor(query).enqueue(new Callback<SpotifyResponse>() {
                                @Override
                                public void onResponse(Response<SpotifyResponse> response, Retrofit retrofit) {
                                    if (response.isSuccess()) {
                                        List<SPTrack> tracks = response.body().getTracks().getItems();
                                        mSpotifyList = new ArrayList<Track>(tracks);
                                        mSpotifyCompleteFlag = true;
                                        buildList();
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Log.d(TAG, "SpotifyAPI search failed");

                                }
                            });
                        }
                        else {
                            mSpotifyCompleteFlag = true;
                        }

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
                                    mSoundcloudCompleteFlag = true;
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
    }

    public void buildList() {
        if (mSpotifyCompleteFlag && mSoundcloudCompleteFlag) {
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
            mSpotifyCompleteFlag = false;
            mSoundcloudCompleteFlag = false;
            updateUI();
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
                Glide.with(getActivity())
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
            PlayerLab playerLab = (PlayerLab) getActivity().getApplicationContext();
            playerLab.setOneTrack(mTrack);
            // Add to recently played
            String now = new Date().toString();
            mTrack.setCreatedAt(now);
            mMusicLab = MusicLab.get(getApplicationContext());
            if (mMusicLab.getTrack(mTrack.getID()) == null) {
                mMusicLab.addTrack(mTrack);
            }
            else {
                mMusicLab.updateTrack(mTrack);
            }
        }

    }

    private class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {

        private List<Track> mTracks;

        public SearchResultAdapter(List<Track> tracks) {
            mTracks = tracks;
        }

        @Override
        public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
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
