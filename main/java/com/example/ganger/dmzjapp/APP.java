package com.example.ganger.dmzjapp;

import android.app.Application;

/**
 * Created by ganger on 2017/6/21.
 */
public class APP extends Application {
    private static APP INSTANCE;
    public static APP getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //AppConfig.getInstance().initAppConfig();
    }
}
