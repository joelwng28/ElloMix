package com.ellomix.android.ellomix.ServerAPI.ServerModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jhurt on 2/19/17.
 */

public class ServerChat {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("most_recent_message")
    @Expose
    private String mostRecentMessage;

//    @SerializedName("playlists")
////    @Expose
//    private List<String> playlists = null;
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    @SerializedName("participants")
    @Expose
    private List<String> participants;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMostRecentMessage() {
        return mostRecentMessage;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public List<String> getParticipants() {
        return participants;
    }
}
