package com.norbinarius.glyphsaber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;

import com.nothing.ketchum.Common;
import com.nothing.ketchum.Glyph;
import com.nothing.ketchum.GlyphManager;

public class GlyphUtil {

    private static GlyphUtil instance;

    public static synchronized GlyphUtil getInstance(){
        if(instance == null)
            instance = new GlyphUtil();

        return instance;
    }

    public void LoadAnimationParameters(Activity context){
        String saberStyle = SharedPreferencesManager.getInstance(context).getSaberStyle();

        if(saberStyle.equals(SettingsActivity.SABER_STYLES[0])){
            MainActivity.glyphAnimationStroboscopic = true;
            MainActivity.randomizeLightBrightness = false;
            MainActivity.isDualChannelActive = false;
        }
        if(saberStyle.equals(SettingsActivity.SABER_STYLES[1])){
            MainActivity.glyphAnimationStroboscopic = false;
            MainActivity.randomizeLightBrightness = true;
            MainActivity.minLightBrightness = 1000;
            MainActivity.isDualChannelActive = false;
        }
        if(saberStyle.equals(SettingsActivity.SABER_STYLES[2])){
            MainActivity.glyphAnimationStroboscopic = false;
            MainActivity.randomizeLightBrightness = true;
            MainActivity.minLightBrightness = 2000;
            MainActivity.isDualChannelActive = false;
        }
        if(saberStyle.equals(SettingsActivity.SABER_STYLES[3])){
            MainActivity.glyphAnimationStroboscopic = true;
            MainActivity.randomizeLightBrightness = true;
            MainActivity.isDualChannelActive = false;
        }
        if(saberStyle.equals(SettingsActivity.SABER_STYLES[4])){
            MainActivity.glyphAnimationStroboscopic = false;
            MainActivity.randomizeLightBrightness = true;
            MainActivity.minLightBrightness = 1500;
            MainActivity.isDualChannelActive = true;
        }

    }

    public void setChannelSizes(GlyphManager mGM){
        /* NOTHING PHONE 1 Running on Nothing OS 3.0
        if (Common.is20111()) { // Phone 1 one channel
            mGM.register(Glyph.DEVICE_20111);
            MainActivity.mainChannelSize = 8;
            MainActivity.mainChannelStartIndex = 7;
            MainActivity.mainChannelLastIndex = 14;
        } */
        if (Common.is22111()) { //Phone 2 dual channel
            mGM.register(Glyph.DEVICE_22111);
            MainActivity.mainChannelSize = 16;
            MainActivity.mainChannelStartIndex = 3;
            MainActivity.mainChannelLastIndex = 18;

            MainActivity.secondaryChannelSize = 8;
            MainActivity.secondaryChannelStartIndex = 25;
            MainActivity.secondaryChannelLastIndex = 32;
        }
        if (Common.is23111()) { //Phone 2a one channel
            mGM.register(Glyph.DEVICE_23111);
            MainActivity.mainChannelSize = 24;
            MainActivity.mainChannelStartIndex = 0;
            MainActivity.mainChannelLastIndex = 23;
        }
        if (Common.is23113()) { //Phone 2a plus one channel
            mGM.register(Glyph.DEVICE_23113);
            MainActivity.mainChannelSize = 24;
            MainActivity.mainChannelStartIndex = 0;
            MainActivity.mainChannelLastIndex = 23;
        }
        if (Common.is24111()) { //Phone 3a/3a pro three channels
            mGM.register(Glyph.DEVICE_24111);
            MainActivity.mainChannelSize = 20;
            MainActivity.mainChannelStartIndex = 0;
            MainActivity.mainChannelLastIndex = 19;

            MainActivity.secondaryChannelSize = 10;
            MainActivity.secondaryChannelStartIndex = 20;
            MainActivity.secondaryChannelLastIndex = 30;

        }
    }

    public void compatibilityCheck(Activity context){
        if(!Build.MODEL.equals(Glyph.DEVICE_22111) && !Build.MODEL.equals(Glyph.DEVICE_23111) &&
                !Build.MODEL.contains(Glyph.DEVICE_24111) &&
                !Build.MODEL.equals(Glyph.DEVICE_23113) && !Build.MODEL.equals(Glyph.DEVICE_22111I)){
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Unsupported phone");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("This app supports only Nothing Phone 2, Phone 2a/2a Plus and 3a/3a pro");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "EXIT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                context.finish();
                            }
                        });
                alertDialog.show();
        }
    }
}
