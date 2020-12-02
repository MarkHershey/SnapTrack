package com.example.snaptrackapp.ui.analytics;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.snaptrackapp.data.Analytics;
import com.example.snaptrackapp.data.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnalyticsViewModel extends ViewModel {

    private final String TAG = "AnalyticsViewModel";
    private final MutableLiveData<ArrayList<Map<String, Long>>> eventAnalyticsListLive;

    public AnalyticsViewModel() {
        eventAnalyticsListLive = new MutableLiveData<>();

        Analytics.fetchTotalTimesPastDays(7, new Listener<List<Map<String, Long>>>() {
            @Override
            public void update(List<Map<String, Long>> maps) {

                for (Map<String, Long> map: maps){

                    for (String s: map.keySet()){
                        Log.d(TAG, s);
                    }
                    for (Long duration: map.values()){
                        Log.d(TAG, String.valueOf(duration));
                    }

                }
                eventAnalyticsListLive.setValue(new ArrayList<>(maps));
            }
        });


    }

    public MutableLiveData<ArrayList<Map<String, Long>>> getEventAnalyticsListLive() {
        return eventAnalyticsListLive;
    }
}