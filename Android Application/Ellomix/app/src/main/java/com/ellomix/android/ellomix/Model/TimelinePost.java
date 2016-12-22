package com.ellomix.android.ellomix.Model;

import com.ellomix.android.ellomix.Model.User;

import java.sql.Time;
import java.util.Date;

/**
 * Created by ATH-AJT2437 on 12/22/2016.
 */

public class TimelinePost {

    private User mCreator;
    private Track mTrack;
    private Date mDateCreated;
    private String mDescription;

    public TimelinePost(User user, Track track, String description) {
        mCreator = user;
        mTrack = track;
        mDateCreated = new Date();
        mDescription = description;
    }

    public User getUser() {
        return mCreator;
    }

    public Track getTrack() {
        return mTrack;
    }

    public Date getDateCreated() {
        return mDateCreated;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
