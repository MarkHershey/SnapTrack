package com.example.snaptrackapp.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snaptrackapp.R;
import com.example.snaptrackapp.data.UserActivityInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ActivitiesFragment extends Fragment {

    private ActivitiesViewModel activitiesViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<UserActivityInfo> activityList;
    FloatingActionButton mFloatingActionButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activitiesViewModel = new ViewModelProvider(this).get(ActivitiesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_activities, container, false);


        activityList = new ArrayList<UserActivityInfo>();
        activityList.add(new UserActivityInfo("Coding is fun", "Work", Color.parseColor("#F2FF49")));
        activityList.add(new UserActivityInfo("Debug is pain", "Life", Color.parseColor("#FF4242")));
        activityList.add(new UserActivityInfo("Automation is awesome", "Work", Color.parseColor("#FB62F6")));
        activityList.add(new UserActivityInfo("CI/CD is fabulous", "Life", Color.parseColor("#645DD7")));
        activityList.add(new UserActivityInfo("Testing is important", "Work", Color.parseColor("#B3FFFC")));
        activityList.add(new UserActivityInfo("Coding is fun", "Work", Color.parseColor("#F2FF49")));
        activityList.add(new UserActivityInfo("Debug is pain", "Life", Color.parseColor("#FF4242")));
        activityList.add(new UserActivityInfo("Automation is awesome", "Work", Color.parseColor("#FB62F6")));
        activityList.add(new UserActivityInfo("CI/CD is fabulous", "Life", Color.parseColor("#645DD7")));
        activityList.add(new UserActivityInfo("Testing is important", "Work", Color.parseColor("#B3FFFC")));
        activityList.add(new UserActivityInfo("Coding is fun", "Work", Color.parseColor("#F2FF49")));
        activityList.add(new UserActivityInfo("Debug is pain", "Life", Color.parseColor("#FF4242")));
        activityList.add(new UserActivityInfo("Automation is awesome", "Work", Color.parseColor("#FB62F6")));
        activityList.add(new UserActivityInfo("CI/CD is fabulous", "Life", Color.parseColor("#645DD7")));
        activityList.add(new UserActivityInfo("Testing is important", "Work", Color.parseColor("#B3FFFC")));
        activityList.add(new UserActivityInfo("Coding is fun", "Work", Color.parseColor("#F2FF49")));
        activityList.add(new UserActivityInfo("Debug is pain", "Life", Color.parseColor("#FF4242")));
        activityList.add(new UserActivityInfo("Automation is awesome", "Work", Color.parseColor("#FB62F6")));
        activityList.add(new UserActivityInfo("CI/CD is fabulous", "Life", Color.parseColor("#645DD7")));
        activityList.add(new UserActivityInfo("Testing is important", "Work", Color.parseColor("#B3FFFC")));
        activityList.add(new UserActivityInfo("Coding is fun", "Work", Color.parseColor("#F2FF49")));
        activityList.add(new UserActivityInfo("Debug is pain", "Life", Color.parseColor("#FF4242")));
        activityList.add(new UserActivityInfo("Automation is awesome", "Work", Color.parseColor("#FB62F6")));
        activityList.add(new UserActivityInfo("CI/CD is fabulous", "Life", Color.parseColor("#645DD7")));
        activityList.add(new UserActivityInfo("Testing is important", "Work", Color.parseColor("#B3FFFC")));

        mRecyclerView = root.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ActivitiesAdapter(activityList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mFloatingActionButton = root.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("ActivityAdapter","Create activity");
//                Toast.makeText(getContext(), "Create new activity", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity() , com.example.snaptrackapp.ui.create_activity.CreateActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }
}