package com.example.today;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TodayCurrentEvent2 extends AppCompatActivity {

    TextView textViewTimer;
    TextView textViewActivityName;
    Button buttonStop;

    long startTime = 0;

    public final static String TIMED_KEY = "package com.example.today.TIMED_KEY";

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;

            textViewTimer.setText(String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_current_event2);

        // get reference to widgets
        textViewTimer = findViewById(R.id.textViewTimer);

        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);


        textViewActivityName = findViewById(R.id.textViewActivityName);
        textViewActivityName.setText("Current Event");

        buttonStop = findViewById(R.id.buttonStop);


        // on imageButtonPlusSign to create new activity
        buttonStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //timerHandler.removeCallbacks(timerRunnable);
                Intent intent_timed = new Intent(TodayCurrentEvent2.this, Today.class);
                intent_timed.putExtra(TIMED_KEY, String.valueOf(textViewTimer));
                startActivity(intent_timed);
                //todo pass data - time
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

}