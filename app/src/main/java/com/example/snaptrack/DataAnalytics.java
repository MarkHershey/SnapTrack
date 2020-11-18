package com.example.snaptrack;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DataAnalytics extends AppCompatActivity {
    Button buttonAddChangeTag;
    Button buttonDataAnalytics;
    Button buttonMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_analytics_sub);

        buttonMainActivity = findViewById(R.id.buttonMainActivity);
        buttonAddChangeTag = findViewById(R.id.buttonAddChangeTag);
        buttonDataAnalytics = findViewById(R.id.buttonDataAnalytics);
        //Ask to scan tag

        //if tag set before bring to change tag settings

        //if tag not set before bring up new tag settings




    }

}
