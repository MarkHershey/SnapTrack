package com.example.snaptrackapp.ui.analytics;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class AnalyticsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AnalyticsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Analytics fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}