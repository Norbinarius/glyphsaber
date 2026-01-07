package com.norbinarius.glyphsaber;

import static android.view.View.INVISIBLE;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.nothing.ketchum.Glyph;

public class SettingsActivity extends AppCompatActivity {
    public static final String[] SABER_STYLES = {"green","blue","red","cg","dual"};
    private Switch switchVolumeHook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        switchVolumeHook = (Switch) findViewById(R.id.switchVolumeBtns);
        RadioGroup radioGroupGender = (RadioGroup) findViewById(R.id.radio_saber_style);
        LoadSettingValues();

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the ID of the selected RadioButton
                RadioButton selectedRadioButton = (RadioButton) findViewById(checkedId);

                SharedPreferencesManager.getInstance(SettingsActivity.this)
                        .setSaberStyle(selectedRadioButton.getTag().toString());
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled initially */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event here
                // e.g. show a dialog, close a drawer, or go to a specific fragment
                //Toast.makeText(SettingsActivity.this, "Back pressed, custom handling!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        switchVolumeHook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesManager.getInstance(SettingsActivity.this).setVolumeHook(isChecked);
            }
        });
    }

    private void LoadSettingValues(){
        if(!Build.MODEL.equals(Glyph.DEVICE_22111) || !Build.MODEL.equals(Glyph.DEVICE_22111I) ||
                !Build.MODEL.contains(Glyph.DEVICE_24111)){
            RadioButton rb_dual = (RadioButton) findViewById(R.id.radio_dual);
            rb_dual.setEnabled(false);
            rb_dual.setVisibility(INVISIBLE);
        }

        RadioGroup radioGroupGender = (RadioGroup) findViewById(R.id.radio_saber_style);
        String saberStyle = SharedPreferencesManager.getInstance(SettingsActivity.this).getSaberStyle();

        if(saberStyle.equals(SABER_STYLES[0])){
            radioGroupGender.check(R.id.radio_green);
        }
        if(saberStyle.equals(SABER_STYLES[1])){
            radioGroupGender.check(R.id.radio_blue);
        }
        if(saberStyle.equals(SABER_STYLES[2])){
            radioGroupGender.check(R.id.radio_red);
        }
        if(saberStyle.equals(SABER_STYLES[3])){
            radioGroupGender.check(R.id.radio_cg);
        }
        if(saberStyle.equals(SABER_STYLES[4])){
            radioGroupGender.check(R.id.radio_dual);
        }
        switchVolumeHook.setChecked(SharedPreferencesManager.getInstance(SettingsActivity.this).getVolumeHook());
    }

}