package com.ellomix.android.ellomix.ServerAPI.ServerModels;

import com.ellomix.android.ellomix.Model.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jhurt on 2/19/17.
 */

public class ServerUser extends User {

//    @SerializedName("chats")
////    @Expose
//    private List<ServerChat> chats = null;

    @SerializedName("id")
//    @Expose
    private String mId;

    @SerializedName("name")
//    @Expose
    private String mName;

    @SerializedName("description")
//    @Expose
    private String mDescription;

//    @SerializedName("photo_url")
////    @Expose
//    private String mPhotoUrl;

//    @SerializedName("top_artists")
////    @Expose
//    private List<Object> mTopArtists = null;

//    @SerializedName("recently_played")
////    @Expose
//    private List<Object> mRecentlyPlayed = null;

    // TODO(jhurt): Replace <String> with <ServerPlaylist> object (need to create it)
//    @SerializedName("playlists")
////    @Expose
//    private List<String> mPlaylists = null;

//    @SerializedName("fave_genres")
////    @Expose
//    private List<String> mFaveGenres = null;

    @SerializedName("createdAt")
//    @Expose
    private String createdAt;

    @SerializedName("updatedAt")
//    @Expose
    private String updatedAt;

}
