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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private EventsAdapter mRecyclerViewAdapter;
    private ArrayList<EventInfo> eventList;

    FloatingActionButton mFloatingActionButton;
    ImageView emptyStateView;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        todayViewModel = new ViewModelProvider(this).get(TodayViewModel.class);
        View root = inflater.inflate(R.layout.fragment_today, container, false);
        // get empty state image view object
        emptyStateView = root.findViewById(R.id.todayEmpty);

        eventList = new ArrayList<EventInfo>();

        // scrolling RecyclerView stuff
        mRecyclerView = root.findViewById(R.id.eventsRecyclerView);
        // set empty adapter to avoid exception
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new EventsAdapter(eventList, getContext()));

        todayViewModel.getEventListLive().observe(getViewLifecycleOwner(), new Observer<ArrayList<EventInfo>>() {
            @Override
            public void onChanged(ArrayList<EventInfo> eventInfos) {
                if (eventInfos.size() != 0) emptyStateView.setVisibility(View.INVISIBLE);
                else emptyStateView.setVisibility(View.VISIBLE);
                mRecyclerViewAdapter = new EventsAdapter(eventInfos, getContext());
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
            }
        });


        // Floating Button stuff
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