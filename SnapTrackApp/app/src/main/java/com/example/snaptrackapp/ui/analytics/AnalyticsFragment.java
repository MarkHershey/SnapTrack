package com.example.snaptrackapp.ui.analytics;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.snaptrackapp.R;

import java.util.ArrayList;
import java.util.Map;

public class AnalyticsFragment extends Fragment {

    private AnalyticsViewModel analyticsViewModel;


    public static AnalyticsFragment newInstance() {
        return new AnalyticsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_analytics, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        analyticsViewModel = new ViewModelProvider(this).get(AnalyticsViewModel.class);
        // TODO: Use the ViewModel
        analyticsViewModel.getEventAnalyticsListLive().observe(getViewLifecycleOwner(), new Observer<ArrayList<Map<String, Long>>>() {
            @Override
            public void onChanged(ArrayList<Map<String, Long>> maps) {
                // TODO on change
            }
        });
    }

}