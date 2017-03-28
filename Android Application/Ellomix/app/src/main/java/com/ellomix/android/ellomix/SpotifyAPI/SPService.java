package com.ellomix.android.ellomix.SpotifyAPI;

import com.ellomix.android.ellomix.SoundCloudAPI.SCConfig;
import com.ellomix.android.ellomix.SoundCloudDataModel.SCTrack;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Akshay on 3/28/17.
 */

public interface SPService {

    @GET("/v1/search?type=track")
    Call<SpotifyResponse> searchFor(@Query("q") String search);

    @GET("/v1/search?offset={off}")
    Call<SpotifyResponse> searchForMore(@Path("off") int offset, @Query("q") String search);
}
