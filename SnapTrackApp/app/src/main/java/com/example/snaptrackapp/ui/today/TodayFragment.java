package com.example.snaptrackapp.ui.today;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snaptrackapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class TodayFragment extends Fragment {

    RecyclerView recyclerView;
    TodayAdapt adapt;

    private TodayViewModel todayViewModel;
    FloatingActionButton mFloatingActionButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        todayViewModel = new ViewModelProvider(this).get(TodayViewModel.class);

        View root = inflater.inflate(R.layout.fragment_today, container, false);

        final TextView textView = root.findViewById(R.id.text_today);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager((root.getContext())));
        adapt = new TodayAdapt(this, getMyList());
        recyclerView.setAdapter(adapt);


        todayViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mFloatingActionButton = root.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO WILL NOT OPEN TO ACTIVITY NEED HELP
                Intent intent = new Intent(getActivity(), com.example.snaptrackapp.ui.today.TodayCurrentEvent.class);
                startActivity(intent);

                //Toast.makeText(getContext(), "Start new event", Toast.LENGTH_SHORT).show();


            }
        });


        return root;
    }

        // todo intent from sub activity - TIMED VALUE
        //Intent intent_timed_time = getIntent();
        //long textViewTimer = intent_timed_time.getLongExtra(TodayCurrentEvent.TIMED_KEY, 0);



    //dummy data to display for now
    private ArrayList<TodayModel> getMyList() {
        ArrayList<TodayModel> models = new ArrayList<>();
        TodayModel m = new TodayModel();
        m.setName("Reading");
        m.setTime("02:00:00");
        models.add(m);

        m = new TodayModel();
        m.setName("Workout");
        m.setTime("01:00:00");
        models.add(m);

        return models;
    }

}
