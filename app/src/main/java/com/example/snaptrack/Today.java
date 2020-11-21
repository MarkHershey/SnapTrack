package com.example.snaptrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Today extends AppCompatActivity {

    Button buttonCreateActivity;
    Button buttonDataAnalytics;
    Button buttonToday;
    EditText editTextValue;
    TextView textViewResult;
    TextView textViewExchangeRate;
    double exchangeRate;
    public final String TAG = "Logcat";
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.mainsharedprefs";
    public static final String RATE_KEY = "Rate_Key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_main);

        buttonCreateActivity = findViewById(R.id.buttonCreateActivity);
        buttonToday = findViewById(R.id.buttonToday);
        buttonDataAnalytics = findViewById(R.id.buttonDataAnalytics);

        //Bring user to AddChangeTag screen
        buttonCreateActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 3.4 Write an Explicit Intent to get to SubActivity
                Intent intent = new Intent(Today.this , CreateActivity.class);
                startActivity(intent);
            }
        });


    }
}