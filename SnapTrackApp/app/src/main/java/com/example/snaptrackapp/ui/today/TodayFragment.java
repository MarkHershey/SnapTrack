package com.example.snaptrackapp.ui.today;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snaptrackapp.R;
import com.example.snaptrackapp.data.EventInfo;
import com.example.snaptrackapp.data.UserActivityInfo;
import com.example.snaptrackapp.ui.activities.ActivitiesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class TodayFragment extends Fragment {

    private final String TAG = "TodayFragment";
    private TodayViewModel todayViewModel;

    private RecyclerView mRecyclerView;
    private ActivitiesAdapter mRecyclerViewAdapter;
    private ArrayList<EventInfo> eventList;

    FloatingActionButton mFloatingActionButton;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        todayViewModel = new ViewModelProvider(this).get(TodayViewModel.class);

        View root = inflater.inflate(R.layout.fragment_today, container, false);

        final TextView textView = root.findViewById(R.id.text_today);

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
                Toast.makeText(getContext(), "Start new event", Toast.LENGTH_SHORT).show();
                // TODO: start new event tracking manually
            }
        });

        return root;
    }
}