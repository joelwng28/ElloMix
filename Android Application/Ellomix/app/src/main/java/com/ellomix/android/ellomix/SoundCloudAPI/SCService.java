package com.ellomix.android.ellomix.SoundCloudAPI;

import com.ellomix.android.ellomix.SoundCloudDataModel.Track;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by alepena01 on 10/27/16.
 */

public interface SCService {


    @GET("/tracks?client_id=" + SCConfig.CLIENT_ID)
    Call<List<Track>> getRecentTracks(@Query("created_at[from]") String date);

    @GET("/tracks?client_id=" + SCConfig.CLIENT_ID)
    Call<List<Track>> searchFor(@Query("q") String search);

}
