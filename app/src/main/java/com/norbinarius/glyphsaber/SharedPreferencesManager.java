package com.norbinarius.glyphsaber;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String APP_PREFS = "Settings";
    private static final String STYLE_KEY = "StyleKey";
    private static final String VOLUME_HOOK_KEY = "VolumeHook";

    private SharedPreferences sharedPrefs;
    private static SharedPreferencesManager instance;



    private SharedPreferencesManager(Context context) {
        sharedPrefs =
                context.getApplicationContext().getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
    }


    public static synchronized SharedPreferencesManager getInstance(Context context){
        if(instance == null)
            instance = new SharedPreferencesManager(context);

        return instance;
    }

    public void setSaberStyle(String saberStyle) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(STYLE_KEY, saberStyle);
        editor.apply();
    }

    public String getSaberStyle() {
        return sharedPrefs.getString(STYLE_KEY, SettingsActivity.SABER_STYLES[0]);
    }

    public void setVolumeHook(boolean state){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(VOLUME_HOOK_KEY, state);
        editor.apply();
    }

    public boolean getVolumeHook(){
        return sharedPrefs.getBoolean(VOLUME_HOOK_KEY, true);
    }
}
