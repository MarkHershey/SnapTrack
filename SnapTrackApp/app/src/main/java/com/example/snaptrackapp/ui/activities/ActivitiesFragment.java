package com.example.snaptrackapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snaptrackapp.R;
import com.example.snaptrackapp.data.DataUtils;
import com.example.snaptrackapp.data.Listener;
import com.example.snaptrackapp.data.UserActivityInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivitiesFragment extends Fragment {

    private final String TAG = "ActivitiesFragment";


    private RecyclerView mRecyclerView;
    private ActivitiesAdapter mRecyclerViewAdapter;
    private ArrayList<UserActivityInfo> activityList;

    FloatingActionButton mFloatingActionButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ActivitiesViewModel activitiesViewModel = new ViewModelProvider(this).get(ActivitiesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_activities, container, false);

        activityList = new ArrayList<>();
        Log.d(TAG, "Fragment onCreateView");

        // scrolling RecyclerView stuff
        mRecyclerView = root.findViewById(R.id.recyclerView);
        // set empty adapter to avoid exception
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new ActivitiesAdapter(activityList, getContext()));

        activitiesViewModel.getActivityListLive().observe(getViewLifecycleOwner(), new Observer<ArrayList<UserActivityInfo>>() {
            @Override
            public void onChanged(ArrayList<UserActivityInfo> userActivityInfoList) {
                mRecyclerViewAdapter = new ActivitiesAdapter(userActivityInfoList, getContext());
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
            }
        });


        // Floating Button stuff
        mFloatingActionButton = root.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"User creating new UserActivity");
                Intent intent = new Intent(getActivity() , NewUserActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Fragment onResume");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "Fragment onActivityCreated");
    }
}
