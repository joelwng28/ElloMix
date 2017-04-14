package com.ellomix.android.ellomix.SpotifyAPI;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Akshay on 3/28/17.
 */

public class SpotifyAPI {

    private static final String CLIENT_ID = "8390a95e6e6a4236a4f40cca17f13150";
    private static final String REDIRECT_URI = "my-spotify-login-one://callback";
    private static final int REQUEST_CODE = 1337;

    private static final Retrofit REST_ADAPTER = new Retrofit.Builder()
            .baseUrl(SPConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final SPService SERVICE = REST_ADAPTER.create(SPService.class);

    public static SPService getService() {
        return SERVICE;
    }

    public static String getClientId() {
        return CLIENT_ID;
    }

    public static String getRedirectUri() {
        return REDIRECT_URI;
    }

    public static int getRequestCode() {
        return REQUEST_CODE;
    }
}
