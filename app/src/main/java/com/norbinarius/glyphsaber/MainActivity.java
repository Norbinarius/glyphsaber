package com.norbinarius.glyphsaber;

import android.content.ComponentName;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.nothing.ketchum.GlyphException;
import com.nothing.ketchum.GlyphFrame;
import com.nothing.ketchum.GlyphManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.view.KeyEvent;
import android.widget.CompoundButton;

import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {
    private GlyphManager mGM = null;
    private GlyphManager.Callback mCallback = null;
    public static int mainChannelStartIndex, mainChannelLastIndex, mainChannelSize;
    public static int secondaryChannelStartIndex, secondaryChannelLastIndex, secondaryChannelSize;
    private final String TAG = "log";
    private static int counter = 0;
    public static long SABER_SPEED = 35;
    private final static int SABER_IDLE_SPEED = 25;
    private boolean isInputActive = true, isVolumeHookActive = true;
    private SoundPool soundPool;
    private int soundIdleId;
    private boolean channelSwitch, isSaberActive;
    public static boolean isDualChannelActive;
    public static boolean glyphAnimationStroboscopic, randomizeLightBrightness;
    public static int minLightBrightness = 500, maxLightBrightness = 4000;
    private SwitchCompat switchBtn;
    private final HandlerThread thread = new HandlerThread("glyphThread");
    private Handler handler;

    final Runnable activateSaber = new Runnable() {
        public void run() {
            //reveal saber
            channelSwitch = false;
            counter = 0;
            int mainChannel = mainChannelStartIndex;
            int secondChannel = secondaryChannelStartIndex;
            int incrementStep = glyphAnimationStroboscopic ? 2 : 1;

            while(mainChannel <= mainChannelLastIndex){
                GlyphFrame.Builder builder = mGM.getGlyphFrameBuilder();
                mainChannel = setChannel(false);
                secondChannel = setChannel(true);
                for(int i = 0; i <= counter; i++){
                    builder.buildChannel(mainChannel, randomizeLightBrightness ?
                            ThreadLocalRandom.current().nextInt(minLightBrightness, maxLightBrightness) :
                            maxLightBrightness);
                    mainChannel += incrementStep;
                }
                //dual
                if(isDualChannelActive && secondChannel <= secondaryChannelSize){
                    for(int i = 0; i <= counter; i++) {
                        builder.buildChannel(secondChannel, randomizeLightBrightness ?
                                ThreadLocalRandom.current().nextInt(minLightBrightness, maxLightBrightness) :
                                maxLightBrightness);
                        secondChannel += incrementStep;
                    }
                }

                mGM.toggle(builder.build());
                counter++;
                channelSwitch = !channelSwitch;
                SystemClock.sleep(SABER_SPEED / (glyphAnimationStroboscopic ? 1 : 2));
            }

            //start idle
            handler.post(idleSaber);
            soundIdleId = soundPool.play(AudioHelper.soundIdle, 1,1,0,-1,1);
            isInputActive = true;

        }
    };

    final Runnable idleSaber = new Runnable() {
        public void run() {
                if(!isSaberActive){
                    mGM.turnOff();
                    return;
                }
                int incrementStep = glyphAnimationStroboscopic ? 2 : 1;
                GlyphFrame.Builder builder = mGM.getGlyphFrameBuilder();
                for(int i = mainChannelStartIndex; i <= mainChannelLastIndex; i += incrementStep){
                    builder.buildChannel(i, randomizeLightBrightness ?
                            ThreadLocalRandom.current().nextInt(minLightBrightness, maxLightBrightness) :
                            maxLightBrightness);
                }
                if(isDualChannelActive){
                    for(int i = secondaryChannelStartIndex; i <= secondaryChannelLastIndex; i += incrementStep){
                        builder.buildChannel(i, randomizeLightBrightness ?
                                ThreadLocalRandom.current().nextInt(minLightBrightness, maxLightBrightness) :
                                maxLightBrightness);
                    }
                }
                mGM.toggle(builder.build());
                SystemClock.sleep(SABER_IDLE_SPEED);
                if(glyphAnimationStroboscopic) {
                    GlyphFrame.Builder builderMirror = mGM.getGlyphFrameBuilder();
                    for (int i = mainChannelStartIndex + 1; i <= mainChannelLastIndex; i += incrementStep) {
                        builderMirror.buildChannel(i, randomizeLightBrightness ?
                                ThreadLocalRandom.current().nextInt(minLightBrightness, maxLightBrightness) :
                                maxLightBrightness);
                    }
                    if(isDualChannelActive){
                        for(int i = secondaryChannelStartIndex + 1; i <= secondaryChannelLastIndex; i += incrementStep){
                            builderMirror.buildChannel(i, randomizeLightBrightness ?
                                    ThreadLocalRandom.current().nextInt(minLightBrightness, maxLightBrightness) :
                                    maxLightBrightness);
                        }
                    }
                    mGM.toggle(builderMirror.build());
                    SystemClock.sleep(SABER_IDLE_SPEED);
                }

                handler.post(idleSaber);
        }
    };

    final Runnable deactivateSaber = new Runnable() {
        public void run() {
            //stop saber
            int mainChannel = mainChannelStartIndex;
            int secondChannel = secondaryChannelStartIndex;
            //stroboscopic animation
            int incrementStep = glyphAnimationStroboscopic ? 2 : 1;
            channelSwitch = true;
            counter = mainChannelSize/incrementStep;
            while (counter > 0) {
                GlyphFrame.Builder builder = mGM.getGlyphFrameBuilder();
                mainChannel = setChannel(false);
                secondChannel = setChannel(true);
                for (int i = counter; i > 0; i--) {
                    builder.buildChannel(mainChannel, randomizeLightBrightness ?
                            ThreadLocalRandom.current().nextInt(minLightBrightness, maxLightBrightness) :
                            maxLightBrightness);
                    mainChannel += incrementStep;
                }
                //dual
                if(isDualChannelActive && secondChannel <= secondaryChannelSize){
                    for(int i = counter; i > 0; i--) {
                        builder.buildChannel(secondChannel, randomizeLightBrightness ?
                                ThreadLocalRandom.current().nextInt(minLightBrightness, maxLightBrightness) :
                                maxLightBrightness);
                        secondChannel += incrementStep;
                    }
                }

                mGM.toggle(builder.build());
                counter--;
                channelSwitch = !channelSwitch;
                SystemClock.sleep(SABER_SPEED / (glyphAnimationStroboscopic ? 1 : 2));
            }
            //clean up
            mGM.turnOff();
            isInputActive = true;

        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        if(isVolumeHookActive) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    if (action == KeyEvent.ACTION_UP && !isSaberActive) {
                        activateSaber();
                        switchBtn.setChecked(true);
                        // TODO: Handle volume up button release
                    }
                    return true; // Mark the event as handled
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    if (action == KeyEvent.ACTION_DOWN && isSaberActive) {
                        deactivateSaber();
                        switchBtn.setChecked(false);
                        // TODO: Handle volume down button press
                    }
                    return true; // Mark the event as handled
                default:
                    return super.dispatchKeyEvent(event);
            }
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    private void activateSaber(){
        if(!isInputActive) return;

        isInputActive = false;
        isSaberActive = true;
        soundPool.play(AudioHelper.soundActivate
                , 1.0f, 1.0f, 0, 0, 1.0f);
        handler.post(activateSaber);
    }

    private void deactivateSaber(){
        if(!isInputActive) return;

        isInputActive = false;
        soundPool.stop(soundIdleId);
        soundPool.play(AudioHelper.soundDeactivate
                , 1.0f, 1.0f, 0, 0, 1.0f);
        isSaberActive = false;
        handler.removeCallbacks(idleSaber);
        handler.post(deactivateSaber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlyphUtil.getInstance().compatibilityCheck(MainActivity.this);
        init();
        initView();

        mGM = GlyphManager.getInstance(getApplicationContext());
        mGM.init(mCallback);

        thread.start();
        handler = new Handler(thread.getLooper());

        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        AudioHelper.LoadAudio(MainActivity.this, soundPool);
        GlyphUtil.getInstance().LoadAnimationParameters(MainActivity.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseResources();
    }

    private void releaseResources(){
        handler.removeCallbacks(idleSaber);
        isInputActive = false;
        isSaberActive = false;
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        mGM.turnOff();
        try {
            mGM.closeSession();
        } catch (GlyphException e) {
            Log.e(TAG, e.getMessage());
        }
        mGM.unInit();
    }

    private void init() {
        mCallback = new GlyphManager.Callback() {
            @Override
            public void onServiceConnected(ComponentName componentName) {
                GlyphUtil.getInstance().setChannelSizes(mGM);
                try {
                    mGM.openSession();
                } catch (GlyphException e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                try {
                    mGM.closeSession();
                } catch (GlyphException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        };
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        isVolumeHookActive = SharedPreferencesManager.getInstance(MainActivity.this).getVolumeHook();
        switchBtn = (SwitchCompat) findViewById(R.id.saberSwitch);
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!isSaberActive) {
                        activateSaber();
                    }
                } else {
                    if (isSaberActive) {
                        deactivateSaber();
                    }
                }
            }
        });
        findViewById(R.id.settingsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseResources();
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private int setChannel(boolean isSecondaryChannel){
        if(glyphAnimationStroboscopic) {
            if(!isSecondaryChannel) {
                return channelSwitch ? mainChannelStartIndex + 1 : mainChannelStartIndex;
            } else {
                return channelSwitch ? secondaryChannelStartIndex + 1 : secondaryChannelStartIndex;
            }
        } else {
            if(!isSecondaryChannel) {
                return mainChannelStartIndex;
            } else {
                return secondaryChannelStartIndex;
            }
        }
    }
}