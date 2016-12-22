package com.ellomix.android.ellomix.Model;

import android.content.Context;
import android.util.Log;

import com.ellomix.android.ellomix.SoundCloudAPI.SCService;
import com.ellomix.android.ellomix.SoundCloudAPI.SoundCloud;
import com.ellomix.android.ellomix.SoundCloudDataModel.SCTrack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ATH-AJT2437 on 12/22/2016.
 */

public class PostLab {

    private static PostLab sPostLab;
    private static final String TAG = "PostLab";

    private List<TimelinePost> mPosts;
    private List<SCTrack> mListItems = new ArrayList<>();


    public static PostLab get(Context context) {
        if (sPostLab == null) {
            sPostLab = new PostLab(context);
        }
        return sPostLab;
    }

    private PostLab(Context context) {
        mPosts = new ArrayList<>();
    }

    public List<TimelinePost> getPosts(){
        return mPosts;
    }

//    public TimelinePost getPost(String id) {
//
//    }

    private int randomNumberGenerator() {
        Random rand = new Random();
        int randomNum = rand.nextInt(4 + 1);
        return randomNum;
    }

    public void generateModel(List<SCTrack> tracks) {
        String[] usersDemo = {"Abe Torres", "Neil Tanner", "Akshay", "Elena Carrasco", "Micah Peoples"};
        String[] messagesDemo = {
                "DOPE",
                "Some sick beats, everyone check it ok",
                "New favourite song",
                "So mellow",
                "Chillax"
        };

        for (SCTrack track: tracks) {
            User poster = new User(usersDemo[randomNumberGenerator()]);
            TimelinePost post = new TimelinePost(poster, track, messagesDemo[randomNumberGenerator()]);
            mPosts.add(post);
        }
    }

}
