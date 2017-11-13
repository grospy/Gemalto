package com.app4each.autodrawer.controller;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vito on 13/11/2017.
 */

public class SettingsManager {

    private static final String INTERVAL = "interval";
    private static final String PAUSED = "paused";

    private static SettingsManager instance;
    public SharedPreferences settings;

    private SettingsManager(Context context){
        settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public static void init(Context context){
        if(instance == null){
            instance = new SettingsManager(context);
        }
    }

    public static SettingsManager getInstance(){
        if(instance == null)
            throw new IllegalStateException("Must call init first");
        return instance;
    }

    public static long getInterval(String type){
        return getInstance().settings.getLong(INTERVAL+type, 1000);
    }

    public static void setInterval(String type, long interval){
        getInstance().settings.edit().putLong(INTERVAL+type, interval).commit();
    }

    public static boolean isPaused(String type){
        return getInstance().settings.getBoolean(PAUSED+type, false);
    }

    public static void setIsPaused(String type, boolean isPaused){
        getInstance().settings.edit().putBoolean(PAUSED+type, isPaused).commit();
    }

}
