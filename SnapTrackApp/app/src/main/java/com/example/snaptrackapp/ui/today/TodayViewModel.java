package com.example.snaptrackapp.ui.today;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.snaptrackapp.data.DataUtils;
import com.example.snaptrackapp.data.EventInfo;
import com.example.snaptrackapp.data.Listener;
import com.example.snaptrackapp.data.UserActivityInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TodayViewModel extends ViewModel {

    private static final String TAG = "TodayViewModel";
    MutableLiveData<ArrayList<EventInfo>> eventListLive;

    public TodayViewModel() {
        eventListLive = new MutableLiveData<>();

        FirebaseUser userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();

        if (userLoggedIn != null) {
            DataUtils.fetchEvents(new Listener<List<EventInfo>>() {
                @Override
                public void update(List<EventInfo> events) {

                    ArrayList<EventInfo> eventList;

                    if (events != null){
                        // Log.d(TAG, "Loaded number of Events: " + events.size());
                        eventList = new ArrayList<>(events);
                        Collections.sort(eventList);
                        Collections.reverse(eventList);
                        for (EventInfo e:eventList) {
                            // Log.d(TAG, e.getEndTimeAsString());
                        }
                    } else {
                        // Log.d(TAG, "Retrieving EventInfo returned null from Firebase");
                        eventList = new ArrayList<>();
                    }
                    eventListLive.setValue(eventList);
                }
            });
        }
    }

    public MutableLiveData<ArrayList<EventInfo>> getEventListLive() {
        return eventListLive;
    }

}
