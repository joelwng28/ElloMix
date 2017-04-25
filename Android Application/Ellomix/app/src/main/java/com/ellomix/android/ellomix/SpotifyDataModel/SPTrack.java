package com.ellomix.android.ellomix.SpotifyDataModel;

import com.ellomix.android.ellomix.Model.Sources;
import com.ellomix.android.ellomix.Model.Track;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by Akshay on 3/28/17.
 */

public class SPTrack extends Track {

    @SerializedName("name")
    private String mTitle;

    @SerializedName("uri")
    private String mStreamUri;

    @SerializedName("album")
    private SPAlbum mAlbum;

    @SerializedName("artists")
    private List<SPArtist> mArtists;

    public SPTrack() {
        super.setSource(Sources.SPOTIFY);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getStreamURL() {
        return mStreamUri;
    }

    public void setStreamURL(String uri) {
        mStreamUri = uri;
    }

    public SPAlbum getAlbum() {
        return mAlbum;
    }

    public void setAlbum(SPAlbum album) {
        this.mAlbum = album;
    }

    public String getBestImageUrl() {
        return mAlbum.getImages().get(0).getUrl();
    }

    @Override
    public String getArtworkURL() {
        return getBestImageUrl();
    }

    public List<SPArtist> getArtists() {
        return mArtists;
    }

    public void setArtists(List<SPArtist> artists) {
        this.mArtists = artists;
    }

    public String getArtist() {
        return getArtistStringList();
    }

    public String getArtistStringList() {
        StringBuffer artistsString = new StringBuffer();
        int numArtists = mArtists.size();
        if (numArtists > 0) {
            artistsString.append(mArtists.get(0).getArtist());
        }
        for (int i = 1; i < numArtists; i++) {
            artistsString.append(", " + mArtists.get(i).getArtist());
        }

        return artistsString.toString();
    }
}
