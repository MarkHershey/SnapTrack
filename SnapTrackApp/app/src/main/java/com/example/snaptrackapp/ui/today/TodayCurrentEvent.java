package com.example.snaptrackapp.ui.today;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snaptrackapp.MainActivity;
import com.example.snaptrackapp.R;


public class TodayCurrentEvent extends AppCompatActivity {

    TextView textViewTimer;
    TextView textViewActivityName;
    Button buttonStop;

    long startTime = 0;

    public final static String TIMED_KEY = "package com.example.snaptrackapp.ui.today.TIMED_KEY";

    //check if the timer is paused due to home button being pressed
    boolean isRunning = false;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;

            textViewTimer.setText(String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds));

            timerHandler.postDelayed(this, 500);

            isRunning = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todaycurrentevent);

        // get reference to widgets
        textViewTimer = findViewById(R.id.textViewTimer);

        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);


        textViewActivityName = findViewById(R.id.textViewActivityName);
        textViewActivityName.setText("Current Event");

        buttonStop = findViewById(R.id.buttonStop);

        if (isRunning == false) {
            timerHandler.postDelayed(timerRunnable, 0);
        }

        // on imageButtonPlusSign to create new activity
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //timerHandler.removeCallbacks(timerRunnable);
                Intent intent_timed = new Intent(TodayCurrentEvent.this, MainActivity.class);
//                intent_timed.putExtra(TIMED_KEY, String.valueOf(textViewTimer));
                startActivity(intent_timed);
                //todo pass data - time
                timerHandler.removeCallbacks(timerRunnable);
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();

        isRunning = false;
        Toast.makeText(TodayCurrentEvent.this, "Timer has stopped", Toast.LENGTH_LONG).show();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(TodayCurrentEvent.this, "There is no back action", Toast.LENGTH_LONG).show();
        return;
    }

}