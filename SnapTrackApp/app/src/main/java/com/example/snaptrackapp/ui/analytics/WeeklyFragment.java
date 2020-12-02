package com.example.snaptrackapp.ui.analytics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.snaptrackapp.R;
import com.example.snaptrackapp.data.DataUtils;
import com.example.snaptrackapp.data.EventInfo;
import com.example.snaptrackapp.data.Listener;
import com.example.snaptrackapp.data.UserActivityInfo;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WeeklyFragment extends Fragment {

    BarChart barChart;
    BarData barData;
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    BarDataSet barDataSet;

    private Date startDate;
    private Date endDate;
    private Calendar c = Calendar.getInstance();

    public WeeklyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weekly_fragment, container, false);

        barChart = view.findViewById(R.id.weekly_barChart);

        getActivities();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        startDate = c.getTime();

        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        endDate = c.getTime();

        BarDataSet barDataSet = new BarDataSet(barEntries, "time");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        barData = new BarData(barDataSet);

        barChart.setData(barData);

        return view;
    }

    private void getActivities() {
        DataUtils.fetchEvents(new Listener<List<EventInfo>>() {
            @Override
            public void update(List<EventInfo> eventInfo) {
                DataUtils.fetchActivitiesSingle(stringUserActivityInfoMap -> getData(eventInfo, stringUserActivityInfoMap));
            }
        }, startDate, endDate);
    };

    private void getData(List<EventInfo> eventInfo, Map<String, UserActivityInfo> activtyInfoMap) {

        ArrayList<EventInfo> week1 = new ArrayList<>();
        ArrayList<EventInfo> week2 = new ArrayList<>();
        ArrayList<EventInfo> week3 = new ArrayList<>();
        ArrayList<EventInfo> week4 = new ArrayList<>();
        Calendar c = Calendar.getInstance();

        for (EventInfo e : eventInfo) {
            c.set(Calendar.DAY_OF_MONTH, 7);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                week1.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.DAY_OF_MONTH, 14);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                week2.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.DAY_OF_MONTH, 21);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                week3.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.DAY_OF_MONTH, 28);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                week4.add(e);
            }
        }

        float[] week1time = new float[week1.size()];
        String[] week1labels = new String[activtyInfoMap.size()];
        ArrayList<String> week1label = new ArrayList<>();
        for (int i = 0; i < week1.size(); i++) {
            long timespent = week1.get(i).getEndTime() - week1.get(i).getStartTime();
            week1time[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            week1label.add(activity);
        }

        for (int j = 0; j < week1label.size(); j++) {
            week1labels[j] = week1label.get(j);
        }

        barDataSet.setStackLabels(week1labels);
        barEntries.add(new BarEntry(2, week1time));

        float[] week2time = new float[week2.size()];
        String[] week2labels = new String[activtyInfoMap.size()];
        ArrayList<String> week2label = new ArrayList<>();
        for (int i = 0; i < week2.size(); i++) {
            long timespent = week2.get(i).getEndTime() - week2.get(i).getStartTime();
            week2time[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            week2label.add(activity);
        }

        for (int j = 0; j < week2label.size(); j++) {
            week2labels[j] = week2label.get(j);
        }

        barDataSet.setStackLabels(week2labels);
        barEntries.add(new BarEntry(2, week2time));

        float[] week3time = new float[week3.size()];
        String[] week3labels = new String[activtyInfoMap.size()];
        ArrayList<String> week3label = new ArrayList<>();
        for (int i = 0; i < week3.size(); i++) {
            long timespent = week3.get(i).getEndTime() - week3.get(i).getStartTime();
            week3time[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            week3label.add(activity);
        }

        for (int j = 0; j < week3label.size(); j++) {
            week3labels[j] = week3label.get(j);
        }

        barDataSet.setStackLabels(week3labels);
        barEntries.add(new BarEntry(2, week3time));

        float[] week4time = new float[week4.size()];
        String[] week4labels = new String[activtyInfoMap.size()];
        ArrayList<String> week4label = new ArrayList<>();
        for (int i = 0; i < week4.size(); i++) {
            long timespent = week4.get(i).getEndTime() - week4.get(i).getStartTime();
            week4time[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            week4label.add(activity);
        }

        for (int j = 0; j < week4label.size(); j++) {
            week4labels[j] = week4label.get(j);
        }

        barDataSet.setStackLabels(week4labels);
        barEntries.add(new BarEntry(2, week4time));
        
    }

    private void getData() {

        barEntries.add(new BarEntry(4, new float[]{2, 5.5f, 4}));
        barEntries.add(new BarEntry(5, new float[]{3, 4.3f, 4}));
        barEntries.add(new BarEntry(6, new float[]{4, 2.3f, 4}));
        barEntries.add(new BarEntry(7, new float[]{1, 5.6f, 4}));
        barEntries.add(new BarEntry(8, new float[]{2, 2.4f, 4}));

    }
}
