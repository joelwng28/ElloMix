package com.ellomix.android.ellomix.SpotifyAPI;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Akshay on 3/28/17.
 */

public class Spotify {

    private static final Retrofit REST_ADAPTER = new Retrofit.Builder()
            .baseUrl(SPConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final SPService SERVICE = REST_ADAPTER.create(SPService.class);

    public static SPService getService() {
        return SERVICE;
    }
}
