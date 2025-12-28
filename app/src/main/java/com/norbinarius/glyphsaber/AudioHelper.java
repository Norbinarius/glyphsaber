package com.norbinarius.glyphsaber;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.widget.Toast;

public class AudioHelper {
    public static int soundIdle, soundActivate, soundDeactivate;
    public static void LoadAudio(Activity context, SoundPool soundPool){
        String saberStyle = SharedPreferencesManager.getInstance(context).getSaberStyle();

        if(saberStyle.equals(SettingsActivity.SABER_STYLES[0])){
            soundIdle = soundPool.load(context, R.raw.g_saber_idle_green, 1);
            soundActivate = soundPool.load(context, R.raw.g_saber_ignition_green, 1);
            soundDeactivate = soundPool.load(context, R.raw.g_saber_deactivation_green, 1);
        }
        if(saberStyle.equals(SettingsActivity.SABER_STYLES[1])){
            soundIdle = soundPool.load(context, R.raw.g_saber_idle_blue, 1);
            soundActivate = soundPool.load(context, R.raw.g_saber_ignition_blue, 1);
            soundDeactivate = soundPool.load(context, R.raw.g_saber_deactivation_blue, 1);
        }
        if(saberStyle.equals(SettingsActivity.SABER_STYLES[2])){
            soundIdle = soundPool.load(context, R.raw.g_saber_idle_red, 1);
            soundActivate = soundPool.load(context, R.raw.g_saber_ignition_defualt, 1);
            soundDeactivate = soundPool.load(context, R.raw.g_saber_deactivation_default, 1);
        }
        if(saberStyle.equals(SettingsActivity.SABER_STYLES[3])){
            soundIdle = soundPool.load(context, R.raw.g_saber_idle_red_cg, 1);
            soundActivate = soundPool.load(context, R.raw.g_saber_ignition_red_cg, 1);
            soundDeactivate = soundPool.load(context, R.raw.g_saber_deactivation_red_cg, 1);
        }
        if(saberStyle.equals(SettingsActivity.SABER_STYLES[4])){
            soundIdle = soundPool.load(context, R.raw.g_saber_idle_dual, 1);
            soundActivate = soundPool.load(context, R.raw.g_saber_ignition_yellow, 1);
            soundDeactivate = soundPool.load(context, R.raw.g_saber_deactivation_yellow, 1);
        }

    }
}
