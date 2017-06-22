package com.example.ganger.dmzjapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

/**
 * Created by ganger on 2017/6/21.
 */
public class AppConfig {


    private static AppConfig INSTANCE=new AppConfig();
    public static AppConfig getInstance() {
        return INSTANCE;
    }
    private AppConfig(){
    }
    private int bigImgSrc;
    public void initAppConfig(){
        SharedPreferences sharedPreferences=APP.getInstance().getSharedPreferences("setting", Context.MODE_PRIVATE);
        bigImgSrc=sharedPreferences.getInt("bigimage",R.drawable.head2);
    }
    public static void setBigImageSrc(int src){
        SharedPreferences sharedPreferences=APP.getInstance().getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("bigimage",src);
        editor.commit();
    }
    public static int getBigImageSrc(){
        return getInstance().bigImgSrc;
    }
}
