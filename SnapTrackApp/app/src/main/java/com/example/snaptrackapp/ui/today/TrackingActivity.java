package com.example.snaptrackapp.ui.today;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snaptrackapp.R;
import com.example.snaptrackapp.data.EventInfo;

public class TrackingActivity extends AppCompatActivity {

    String activityName;
    String AID;
    int color;

    long timeStartMillis;
    long timeEndMillis;
    int trackedTimeSeconds;
    EventInfo thisEvent;

    TextView activityNameTextView;
    Chronometer theChronometer;
    Button stopButton;
    View backgroundView;

    @Override
    public void onBackPressed() {
        Toast.makeText(TrackingActivity.this, "Please stop current tracking before leaving.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        // get view object references
        activityNameTextView = findViewById(R.id.currentTrackingActivity);
        theChronometer = findViewById(R.id.chronometer);
        stopButton = findViewById(R.id.stopTrackingButton);
        backgroundView = (View) activityNameTextView.getParent();

        // start timer
        theChronometer.start();
        timeStartMillis = System.currentTimeMillis();
        // Store data
        // SharedPreferences sharedPref = TrackingActivity.this.getPreferences(Context.MODE_PRIVATE);
        // SharedPreferences.Editor editor = sharedPref.edit();
        // editor.putLong("timeStart", timeStartMillis);
        // editor.apply();

        // get data from intent
        Intent intent = getIntent();
        activityName = intent.getStringExtra("activityName");
        AID = intent.getStringExtra("AID");
        color = Integer.parseInt(intent.getStringExtra("color"));


        // set views
        activityNameTextView.setText(activityName);
        activityNameTextView.setTextColor(color);
        // backgroundView.setBackgroundColor(color);

        // set OnClickListener for stop button
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTracking();
            }
        });


    }

    public void stopTracking(){
        theChronometer.stop();
        timeEndMillis = System.currentTimeMillis();
        trackedTimeSeconds = (int) (timeEndMillis - timeStartMillis) / 1000;
        Toast.makeText(TrackingActivity.this, String.valueOf(trackedTimeSeconds)+"s", Toast.LENGTH_LONG).show();
        // create new Event Object
        thisEvent = new EventInfo(AID, timeStartMillis, timeEndMillis);
        // Submit Event to Firebase
        EventInfo.add(thisEvent);
        // dismiss this activity
        finish();
    }





}