package com.example.snaptrackapp.ui.analytics;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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
import com.firebase.ui.auth.data.model.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DailyFragment extends Fragment {

    BarChart barChart;
    BarData barData;
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    private  Date startDate;
    private Date endDate;
    private Calendar c = Calendar.getInstance();

    public DailyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_fragment, container, false);

        barChart = view.findViewById(R.id.daily_barChart);
        Log.v("logcaat","start daily fragment");
        c.set(Calendar.HOUR_OF_DAY, 0);
        startDate = c.getTime();

        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        endDate = c.getTime();

//        getData();
        barEntries.add(new BarEntry(4, new float[]{2, 5.5f, 4}));
        barEntries.add(new BarEntry(5, new float[]{3, 4.3f, 4}));
        barEntries.add(new BarEntry(6, new float[]{4, 2.3f, 4}));
        barEntries.add(new BarEntry(7, new float[]{1, 5.6f, 4}));
        barEntries.add(new BarEntry(8, new float[]{2, 2.4f, 4}));
        Log.v("logcaat",barEntries.toString());

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
                DataUtils.fetchActivitiesSingle(new Listener<Map<String, UserActivityInfo>>() {
                    @Override
                    public void update(Map<String, UserActivityInfo> stringUserActivityInfoMap) {
                        getData(eventInfo, stringUserActivityInfoMap);
                    }
                });
            }
        });
    };

    private void getData(List<EventInfo> eventInfo, Map<String, UserActivityInfo> activtyInfoMap) {

        for (EventInfo e : eventInfo) {

        }

    }

    private void getData() {



    }
}