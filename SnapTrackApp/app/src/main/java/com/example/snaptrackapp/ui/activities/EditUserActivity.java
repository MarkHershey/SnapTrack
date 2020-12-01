package com.example.snaptrackapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.snaptrackapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class EditUserActivity extends AppCompatActivity {

    private static final String TAG = "EditUserActivity";
    EditText nameEditView;
    EditText categoryEditView;
    SeekBar aBar;
    SeekBar rBar;
    SeekBar gBar;
    SeekBar bBar;
    TextView colorDisplay;
    TextView aidView;
    int intColor;
    int intA;
    int intR;
    int intG;
    int intB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_activity);

        nameEditView = findViewById(R.id.nameEdit);
        categoryEditView = findViewById(R.id.categoryEdit);
        aBar = findViewById(R.id.aBar);
        rBar = findViewById(R.id.rBar);
        gBar = findViewById(R.id.gBar);
        bBar = findViewById(R.id.bBar);
        colorDisplay = findViewById(R.id.colorDisplay);
        aidView = findViewById(R.id.aidLabel);

        Intent intent = getIntent();
        String activityName = intent.getStringExtra("activityName");
        String category = intent.getStringExtra("category");
        String color = intent.getStringExtra("color");
        String AID = intent.getStringExtra("AID");

        intColor = Integer.parseInt(color);
        colorDisplay.setBackgroundColor(intColor);

        intA = (intColor >> 24) & 0xff;
        intR = (intColor >> 16) & 0xff;
        intG = (intColor >>  8) & 0xff;
        intB = (intColor      ) & 0xff;

        nameEditView.setText(activityName);
        categoryEditView.setText(category);

        aBar.setProgress(intA);
        rBar.setProgress(intR);
        gBar.setProgress(intG);
        bBar.setProgress(intB);

        aidView.setText("Activity ID: " + AID);

        aBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >=0 && progress <= 255){
                    intA = progress;
                }
                updateColorDisplay();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        rBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >=0 && progress <= 255){
                    intR = progress;
                }
                updateColorDisplay();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        gBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >=0 && progress <= 255){
                    intG = progress;
                }
                updateColorDisplay();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        bBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >=0 && progress <= 255){
                    intB = progress;
                }
                updateColorDisplay();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    private void updateColorDisplay() {
        intColor = (intA & 0xff) << 24 | (intR & 0xff) << 16 | (intG & 0xff) << 8 | (intB & 0xff);
        colorDisplay.setBackgroundColor(intColor);
    }







}