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

public class MonthlyFragment extends Fragment {

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList<BarEntry> barEntries = new ArrayList<>();

    private Date startDate;
    private Date endDate;
    private final Calendar c = Calendar.getInstance();

    public MonthlyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.monthly_fragment, container, false);

        barChart = view.findViewById(R.id.monthly_barChart);

        getActivities();

        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        startDate = c.getTime();

        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
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

        ArrayList<EventInfo> monday = new ArrayList<>();
        ArrayList<EventInfo> tuesday = new ArrayList<>();
        ArrayList<EventInfo> weds = new ArrayList<>();
        ArrayList<EventInfo> thurs = new ArrayList<>();
        Calendar c = Calendar.getInstance();

        for (EventInfo e : eventInfo) {
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            if (new Date(e.getEndTime()).before(c.getTime())) {
                monday.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.DAY_OF_MONTH, 14);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                tuesday.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.DAY_OF_MONTH, 21);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                weds.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.DAY_OF_MONTH, Calendar.THURSDAY);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                thurs.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                friday.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                saturday.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                sunday.add(e);
            }
        }

        float[] montime = new float[monday.size()];
        String[] monlabels = new String[activtyInfoMap.size()];
        ArrayList<String> monlabel = new ArrayList<>();
        for (int i = 0; i < monday.size(); i++) {
            long timespent = monday.get(i).getEndTime() - monday.get(i).getStartTime();
            montime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            monlabel.add(activity);
        }

        for (int j = 0; j < monlabel.size(); j++) {
            monlabels[j] = monlabel.get(j);
        }

        barDataSet.setStackLabels(monlabels);
        barEntries.add(new BarEntry(2, montime));

        float[] tuestime = new float[tuesday.size()];
        String[] tueslabels = new String[activtyInfoMap.size()];
        ArrayList<String> tueslabel = new ArrayList<>();
        for (int i = 0; i < tuesday.size(); i++) {
            long timespent = tuesday.get(i).getEndTime() - tuesday.get(i).getStartTime();
            tuestime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            tueslabel.add(activity);
        }

        for (int j = 0; j < tueslabel.size(); j++) {
            tueslabels[j] = tueslabel.get(j);
        }

        barDataSet.setStackLabels(tueslabels);
        barEntries.add(new BarEntry(2, tuestime));

        float[] wedtime = new float[weds.size()];
        String[] wedlabels = new String[activtyInfoMap.size()];
        ArrayList<String> wedlabel = new ArrayList<>();
        for (int i = 0; i < weds.size(); i++) {
            long timespent = weds.get(i).getEndTime() - weds.get(i).getStartTime();
            wedtime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            wedlabel.add(activity);
        }

        for (int j = 0; j < wedlabel.size(); j++) {
            wedlabels[j] = wedlabel.get(j);
        }

        barDataSet.setStackLabels(wedlabels);
        barEntries.add(new BarEntry(2, wedtime));

        float[] thurtime = new float[thurs.size()];
        String[] thurlabels = new String[activtyInfoMap.size()];
        ArrayList<String> thurlabel = new ArrayList<>();
        for (int i = 0; i < thurs.size(); i++) {
            long timespent = thurs.get(i).getEndTime() - thurs.get(i).getStartTime();
            thurtime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            thurlabel.add(activity);
        }

        for (int j = 0; j < thurlabel.size(); j++) {
            thurlabels[j] = thurlabel.get(j);
        }

        barDataSet.setStackLabels(thurlabels);
        barEntries.add(new BarEntry(2, thurtime));

        float[] fritime = new float[friday.size()];
        String[] frilabels = new String[activtyInfoMap.size()];
        ArrayList<String> frilabel = new ArrayList<>();
        for (int i = 0; i < friday.size(); i++) {
            long timespent = friday.get(i).getEndTime() - friday.get(i).getStartTime();
            fritime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            frilabel.add(activity);
        }

        for (int j = 0; j < frilabel.size(); j++) {
            frilabels[j] = frilabel.get(j);
        }

        barDataSet.setStackLabels(frilabels);
        barEntries.add(new BarEntry(2, fritime));

        float[] saturtime = new float[saturday.size()];
        String[] saturlabels = new String[activtyInfoMap.size()];
        ArrayList<String> saturlabel = new ArrayList<>();
        for (int i = 0; i < saturday.size(); i++) {
            long timespent = saturday.get(i).getEndTime() - saturday.get(i).getStartTime();
            saturtime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            saturlabel.add(activity);
        }

        for (int j = 0; j < saturlabel.size(); j++) {
            saturlabels[j] = saturlabel.get(j);
        }

        barDataSet.setStackLabels(saturlabels);
        barEntries.add(new BarEntry(2, saturtime));

        float[] suntime = new float[sunday.size()];
        String[] sunlabels = new String[activtyInfoMap.size()];
        ArrayList<String> sunlabel = new ArrayList<>();
        for (int i = 0; i < sunday.size(); i++) {
            long timespent = sunday.get(i).getEndTime() - sunday.get(i).getStartTime();
            suntime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            sunlabel.add(activity);
        }

        for (int j = 0; j < sunlabel.size(); j++) {
            sunlabels[j] = sunlabel.get(j);
        }

        barDataSet.setStackLabels(sunlabels);
        barEntries.add(new BarEntry(2, suntime));
    }

    private void getData() {

        barEntries.add(new BarEntry(4, new float[]{2, 5.5f, 4}));
        barEntries.add(new BarEntry(5, new float[]{3, 4.3f, 4}));
        barEntries.add(new BarEntry(6, new float[]{4, 2.3f, 4}));
        barEntries.add(new BarEntry(7, new float[]{1, 5.6f, 4}));
        barEntries.add(new BarEntry(8, new float[]{2, 2.4f, 4}));

    }
}
