package com.ellomix.android.ellomix.Model;

import android.content.Context;
import android.text.format.DateUtils;

import com.ellomix.android.ellomix.Activities.LoginActivity;
import com.ellomix.android.ellomix.SoundCloudDataModel.SCTrack;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by ATH-AJT2437 on 12/22/2016.
 */

public class TimelinePost {

    private User mCreator;
    private Track mTrack;
    private Date mDatePosted;
    private String mDescription;
    private ArrayList<Comment> mComments;

    public TimelinePost(User user, SCTrack track, String description) {
        mCreator = user;
        mTrack = track;
        mDatePosted = new Date();
        mDescription = description;
        mComments = new ArrayList<>();
    }

    public User getUser() {
        return mCreator;
    }

    public Track getTrack() {
        return mTrack;
    }

    public Date getDateCreated() {
        return mDatePosted;
    }

    public String getSinceCreated() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String trackCreated = mTrack.getCreatedAt().substring(0, 19).replaceAll("/", "-");
        Date dateCreated = formatter.parse(trackCreated);
        long createdMillis = dateCreated.getTime();
        long postedMillis = mDatePosted.getTime() + 2000;
        long diff = postedMillis - createdMillis;
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60) {
            if (seconds == 1)
                return Long.toString(seconds) + " sec ago";
            else
                return Long.toString(seconds) + " secs ago";
        }
        else if (minutes < 60) {
            if (minutes == 1)
                return Long.toString(minutes) + " min ago";
            else
                return Long.toString(minutes) + " mins ago";
        }
        else if (hours < 24) {
            if (hours == 1)
                return Long.toString(hours) + " hrs ago";
            else
                return Long.toString(hours) + " hrs ago";
        }
        else {
            if (days == 1)
                return Long.toString(days) + " day ago";
            else
                return Long.toString(days) + " days ago";
        }
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public ArrayList<Comment> getCommentList() {
        return mComments;
    }

    public void addComment (Comment comment) {
        mComments.add(comment);
    }

    public void setComments (ArrayList<Comment> comments) {
        mComments = comments;
    }
}
