package com.example.snaptrackapp.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snaptrackapp.R;
import com.example.snaptrackapp.UserActivity;

import java.util.ArrayList;

public class ActivitiesFragment extends Fragment {

    private ActivitiesViewModel activitiesViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<UserActivity> activityList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activitiesViewModel = new ViewModelProvider(this).get(ActivitiesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_activities, container, false);

        final TextView textView = root.findViewById(R.id.text_activities);
        activitiesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        activityList = new ArrayList<UserActivity>();

        activityList.add(new UserActivity("00001", "Workout", "WO", "blue"));
        activityList.add(new UserActivity("00002", "Study", "ST", "blue"));
        activityList.add(new UserActivity("00003", "Watch Movie", "MO", "blue"));
        activityList.add(new UserActivity("00004", "Cooking", "CO", "blue"));
        activityList.add(new UserActivity("00005", "Laundry", "LA", "blue"));
        activityList.add(new UserActivity("00005", "Math", "LA", "blue"));
        activityList.add(new UserActivity("00005", "Coding", "LA", "blue"));
        activityList.add(new UserActivity("00005", "Sleep", "LA", "blue"));
        activityList.add(new UserActivity("00005", "Chill", "LA", "blue"));
        activityList.add(new UserActivity("00005", "Music", "LA", "blue"));
        activityList.add(new UserActivity("00005", "Social", "LA", "blue"));
        activityList.add(new UserActivity("00005", "Dummy", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));
        activityList.add(new UserActivity("00005", "XYZ", "LA", "blue"));

        mRecyclerView = root.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ActivitiesAdapter(activityList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return root;
    }
}