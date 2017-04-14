package com.ellomix.android.ellomix.SoundCloudAPI;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by alepena01 on 10/27/16.
 */

public class SoundCloudAPI {

    private static final Retrofit REST_ADAPTER = new Retrofit.Builder()
            .baseUrl(SCConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final SCService SERVICE = REST_ADAPTER.create(SCService.class);

    public static SCService getService() {
        return SERVICE;
    }

}
