package com.example.snaptrackapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.snaptrackapp.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;

public class NewUserActivity extends AppCompatActivity {

    private static final String TAG = "NewUserActivity";
    Random rand;
    EditText nameEditView;
    EditText categoryEditView;
    SeekBar aBar;
    SeekBar rBar;
    SeekBar gBar;
    SeekBar bBar;
    TextView colorDisplay;
    Button saveButton;
    Button cancelButton;

    int intColor;
    int intA;
    int intR;
    int intG;
    int intB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_activity);

        // get view references
        nameEditView = findViewById(R.id.nameEdit);
        categoryEditView = findViewById(R.id.categoryEdit);
        aBar = findViewById(R.id.aBar);
        rBar = findViewById(R.id.rBar);
        gBar = findViewById(R.id.gBar);
        bBar = findViewById(R.id.bBar);
        colorDisplay = findViewById(R.id.colorDisplay);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        // initiate color slider
        initRandomColor();

        // color slider listeners
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

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Submit new activity to Firebase
            }
        });





    }

    private void initRandomColor() {
        rand = new Random(new Date().getTime());
        int min = 0;
        int max = 255;
        int randomR = rand.nextInt((max - min) + 1) + min;
        int randomG = rand.nextInt((max - min) + 1) + min;
        int randomB = rand.nextInt((max - min) + 1) + min;
        intA = 255;
        intR = randomR;
        intG = randomG;
        intB = randomB;
        updateColorDisplay();
        aBar.setProgress(intA);
        rBar.setProgress(intR);
        gBar.setProgress(intG);
        bBar.setProgress(intB);
    }

    private void updateColorDisplay() {
        intColor = (intA & 0xff) << 24 | (intR & 0xff) << 16 | (intG & 0xff) << 8 | (intB & 0xff);
        colorDisplay.setBackgroundColor(intColor);
    }

}