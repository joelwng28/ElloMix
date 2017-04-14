package com.ellomix.android.ellomix.Model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ATH-AJT2437 on 1/10/2017.
 */

public class Comment implements Parcelable {
    private User mCommenter;
    private String mText;

    public Comment(Parcel in) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.mCommenter = in.readTypedObject(User.CREATOR);
        }
        this.mText = in.readString();
    }

    public Comment(User user, String text) {
        mCommenter = user;
        mText = text;
    }

    public User getCommenter() {
        return mCommenter;
    }

    public String getText() {
        return mText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dest.writeTypedObject(mCommenter, 0);
        }
        dest.writeString(mText);
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
