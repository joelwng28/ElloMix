package com.ellomix.android.ellomix.Activities;

import android.app.Application;
import android.content.Context;

/**
 * Created by ATH-AJT2437 on 1/16/2017.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
