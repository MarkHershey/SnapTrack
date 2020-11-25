package com.example.today;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class TodayCurrentEvent extends AppCompatActivity {
    Button buttonStop;
    TextView textViewActivityName;
    TextView textViewTimer;

    boolean timerStarted = true;

    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;

    //TODO edit timer for today and todaycurrentevent


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todaycurrentevent);

        // get reference to widgets
        buttonStop = findViewById(R.id.buttonStop);
        textViewActivityName = findViewById(R.id.textViewActivityName);
        textViewTimer = findViewById(R.id.textViewTimer);

        timer = new Timer();

        // on Stop Button to create new activity
        buttonStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(TodayCurrentEvent.this, Today.class);
                startActivity(intent);

                timerTask.cancel();
            }
        });

    }


    private void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        time++;
                        textViewTimer.setText(getTimerText());
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }


    private String getTimerText()
    {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
    }


}




