package com.example.today;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Today extends AppCompatActivity{

    RecyclerView recyclerView;
    Adapt adapt;

    ImageButton imageButtonPlusSign;


    //ProgressBar progressBarTimeBlock;
    //TextView textViewInTimeBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        // get reference to widgets
        imageButtonPlusSign = findViewById(R.id.imageButtonPlusSign);

        //progressBarTimeBlock = findViewById(R.id.progressBarTimeBlock);
        //textViewInTimeBlock = findViewById(R.id.textViewInTimeBlock);
        //Animation animation = (Animation) AnimationUtils.loadAnimation(this, R.anim.animate_progress_bar);
        //progressBarTimeBlock.setProgress(100);
        //progressBarTimeBlock.startAnimation(animation); //remove this

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapt = new Adapt(this,getMyList());
        recyclerView.setAdapter(adapt);

        Intent intent_timed = getIntent();
        long textViewTimer = intent_timed.getLongExtra(TodayCurrentEvent2.TIMED_KEY, 0);
        //todo need to pass textViewTimer to array

        // on imageButtonPlusSign to create new activity
        imageButtonPlusSign.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Today.this, TodayCurrentEvent2.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<Model> getMyList(){
        ArrayList<Model> models = new ArrayList<>();
        Model m = new Model();
        m.setName("Reading");
        m.setTime("02:00:00");
        models.add(m);

        m = new Model();
        m.setName("Workout");
        m.setTime("01:00:00");
        models.add(m);

        return models;
    }
}

