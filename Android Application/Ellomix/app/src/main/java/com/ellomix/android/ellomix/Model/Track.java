package com.ellomix.android.ellomix.Model;

import javax.xml.transform.Source;

/**
 * Created by ATH-AJT2437 on 12/22/2016.
 */

public class Track {

    private String mTitle;
    private String mArtist;
    private String mID;
    private String mCreatedAt;
    private String mStreamURL;
    private String mArtworkURL;
    private String mTime;
    private Sources mSource = Sources.SOUNDCLOUD;

    public Track(){
    }

    public Track(String title, String artist, String id, String date, String url, String time) {
        mTitle = title;
        mArtist = artist;
        mID = id;
        mCreatedAt = date;
        mStreamURL = url;
        mTime = time;
    }

    public Track(String title, String artist, String id) {
        mTitle = title;
        mArtist = artist;
        mID = id;
        mCreatedAt = "";
        mStreamURL = "";
        mTime = "";
    }

    public Sources getSource() {
        return mSource;
    }

    public void setSource(Sources source) {
        mSource = source;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public String getID() {
        return mID;
    }

    public void setID(String id) {
        mID = id;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public void setStreamURL(String url) {
        mStreamURL = url;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }

    public void setArtworkURL(String artworkURL) {
        mArtworkURL = artworkURL;
    }

    public String getTime() {
        return mTime;
    }
}
