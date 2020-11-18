package com.example.snaptrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button buttonAddChangeTag;
    Button buttonDataAnalytics;
    Button buttonMainActivity;
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
        setContentView(R.layout.home_main);

        buttonMainActivity = findViewById(R.id.buttonMainActivity);
        buttonAddChangeTag = findViewById(R.id.buttonAddChangeTag);
        buttonDataAnalytics = findViewById(R.id.buttonDataAnalytics);

        //Bring user to tag modification screen
        buttonAddChangeTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 3.4 Write an Explicit Intent to get to SubActivity
                Intent intent = new Intent(MainActivity.this , AddChangeTag.class);
                startActivity(intent);
            }
        });


    }
}