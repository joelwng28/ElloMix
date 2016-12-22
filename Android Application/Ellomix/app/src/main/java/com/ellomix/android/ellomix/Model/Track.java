package com.ellomix.android.ellomix.Model;

/**
 * Created by ATH-AJT2437 on 12/22/2016.
 */

public class Track {

    private String mTitle;
    private String mArtist;
    private int mID;
    private String mCreatedAt;
    private String mStreamURL;
    private String mTime;

    public Track(){

    }

    public Track(String title, String artist, int id, String date, String url, String time) {
        mTitle = title;
        mArtist = artist;
        mID = id;
        mCreatedAt = date;
        mStreamURL = url;
        mTime = time;
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

    public int getID() {
        return mID;
    }

    public void setID(int id) {
        mID = id;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public void setStreamURL(String url) {
        mStreamURL = url;
    }

    public String getTime() {
        return mTime;
    }
}
