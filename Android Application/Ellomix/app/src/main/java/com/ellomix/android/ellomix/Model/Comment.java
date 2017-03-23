package com.ellomix.android.ellomix.Model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ATH-AJT2437 on 1/10/2017.
 */

public class Comment implements Parcelable {
    private User mUser;
    private String mCommentText;

    public Comment(Parcel in) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.mUser = in.readTypedObject(User.CREATOR);
        }
        this.mCommentText = in.readString();
    }

    public Comment(User user, String text) {
        mUser = user;
        mCommentText = text;
    }

    public User getUser() {
        return mUser;
    }

    public String getText() {
        return mCommentText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dest.writeTypedObject(mUser, 0);
        }
        dest.writeString(mCommentText);
    }

    static final Parcelable.Creator<Comment> CREATOR =
            new Parcelable.Creator<Comment>() {
                @Override
                public Comment createFromParcel(Parcel source) {
                    return new Comment(source);
                }

                @Override
                public Comment[] newArray(int size) {
                    return new Comment[size];
                }
            };
}
