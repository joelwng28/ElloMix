package com.ellomix.android.ellomix.Model;

/**
 * Created by ATH-AJT2437 on 1/10/2017.
 */

public class Comment {
    private User mUser;
    private String mCommentText;

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
}
