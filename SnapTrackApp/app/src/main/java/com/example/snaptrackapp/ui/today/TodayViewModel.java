package com.example.snaptrackapp.ui.today;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class TodayViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    ArrayList<String> today_events = new ArrayList<>();

    public TodayViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Today fragment");

        today_events.add("Gaming");
        today_events.add("Workout");
        today_events.add("Lunch");
        today_events.add("School project");
        today_events.add("Dinner");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public ArrayList<String> getToday_events() {
        return today_events;
    }
}
