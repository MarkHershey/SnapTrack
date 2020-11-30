package com.example.snaptrackapp.ui.activities;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.snaptrackapp.data.DataUtils;
import com.example.snaptrackapp.data.Listener;
import com.example.snaptrackapp.data.UserActivityInfo;

import java.util.ArrayList;
import java.util.Map;

public class ActivitiesViewModel extends ViewModel {

    private static final String TAG = "ActivitiesViewModel";
    MutableLiveData<ArrayList<UserActivityInfo>> activityListLive;

    public ActivitiesViewModel() {
        activityListLive = new MutableLiveData<>();

        DataUtils.fetchActivities(new Listener<Map<String, UserActivityInfo>>(){
            @Override
            public void update(Map<String, UserActivityInfo> activities){

                ArrayList<UserActivityInfo> activityList;

                if (activities != null){
                    Log.d(TAG, "Loaded number of UserActivity: " + activities.size());
                    activityList = new ArrayList<>(activities.values());
                } else {
                    Log.d(TAG, "Retrieving UserActivityInfo returned null from Firebase");
                    activityList = new ArrayList<>();
                }

                activityListLive.setValue(activityList);
            }
        });

    }

    public MutableLiveData<ArrayList<UserActivityInfo>> getActivityListLive() {
        return activityListLive;
    }

}
