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

        ArrayList<EventInfo> january = new ArrayList<>();
        ArrayList<EventInfo> february = new ArrayList<>();
        ArrayList<EventInfo> march = new ArrayList<>();
        ArrayList<EventInfo> april = new ArrayList<>();
        ArrayList<EventInfo> may = new ArrayList<>();
        ArrayList<EventInfo> june = new ArrayList<>();
        ArrayList<EventInfo> july = new ArrayList<>();
        ArrayList<EventInfo> august = new ArrayList<>();
        ArrayList<EventInfo> september = new ArrayList<>();
        ArrayList<EventInfo> october = new ArrayList<>();
        ArrayList<EventInfo> november = new ArrayList<>();
        ArrayList<EventInfo> december = new ArrayList<>();
        Calendar c = Calendar.getInstance();

        for (EventInfo e : eventInfo) {
            c.set(Calendar.MONTH, 0);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                january.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.MONTH, 1);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                february.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.MONTH, 2);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                march.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.MONTH, 3);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                april.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.MONTH, 4);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                may.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.MONTH, 5);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                june.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.MONTH, 6);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                july.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.MONTH, 7);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                august.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.MONTH, 8);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                september.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.MONTH, 9);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                october.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.MONTH, 10);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                november.add(e);
            }
        }

        for (EventInfo e : eventInfo) {
            c.set(Calendar.MONTH, 11);
            if (new Date(e.getEndTime()).before(c.getTime())) {
                december.add(e);
            }
        }

        float[] jantime = new float[january.size()];
        String[] janlabels = new String[activtyInfoMap.size()];
        ArrayList<String> janlabel = new ArrayList<>();
        for (int i = 0; i < january.size(); i++) {
            long timespent = january.get(i).getEndTime() - january.get(i).getStartTime();
            jantime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            janlabel.add(activity);
        }

        for (int j = 0; j < janlabel.size(); j++) {
            janlabels[j] = janlabel.get(j);
        }

        barDataSet.setStackLabels(janlabels);
        barEntries.add(new BarEntry(2, jantime));

        float[] febtime = new float[february.size()];
        String[] feblabels = new String[activtyInfoMap.size()];
        ArrayList<String> feblabel = new ArrayList<>();
        for (int i = 0; i < february.size(); i++) {
            long timespent = february.get(i).getEndTime() - february.get(i).getStartTime();
            febtime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            feblabel.add(activity);
        }

        for (int j = 0; j < feblabel.size(); j++) {
            feblabels[j] = feblabel.get(j);
        }

        barDataSet.setStackLabels(feblabels);
        barEntries.add(new BarEntry(2, febtime));

        float[] martime = new float[march.size()];
        String[] marlabels = new String[activtyInfoMap.size()];
        ArrayList<String> marlabel = new ArrayList<>();
        for (int i = 0; i < march.size(); i++) {
            long timespent = march.get(i).getEndTime() - march.get(i).getStartTime();
            martime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            marlabel.add(activity);
        }

        for (int j = 0; j < marlabel.size(); j++) {
            marlabels[j] = marlabel.get(j);
        }

        barDataSet.setStackLabels(marlabels);
        barEntries.add(new BarEntry(2, martime));

        float[] aprtime = new float[april.size()];
        String[] aprlabels = new String[activtyInfoMap.size()];
        ArrayList<String> aprlabel = new ArrayList<>();
        for (int i = 0; i < april.size(); i++) {
            long timespent = april.get(i).getEndTime() - april.get(i).getStartTime();
            aprtime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            aprlabel.add(activity);
        }

        for (int j = 0; j < aprlabel.size(); j++) {
            aprlabels[j] = aprlabel.get(j);
        }

        barDataSet.setStackLabels(aprlabels);
        barEntries.add(new BarEntry(2, aprtime));

        float[] maytime = new float[may.size()];
        String[] maylabels = new String[activtyInfoMap.size()];
        ArrayList<String> maylabel = new ArrayList<>();
        for (int i = 0; i < may.size(); i++) {
            long timespent = may.get(i).getEndTime() - may.get(i).getStartTime();
            maytime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            maylabel.add(activity);
        }

        for (int j = 0; j < maylabel.size(); j++) {
            maylabels[j] = maylabel.get(j);
        }

        barDataSet.setStackLabels(maylabels);
        barEntries.add(new BarEntry(2, maytime));

        float[] junetime = new float[june.size()];
        String[] junelabels = new String[activtyInfoMap.size()];
        ArrayList<String> junelabel = new ArrayList<>();
        for (int i = 0; i < june.size(); i++) {
            long timespent = june.get(i).getEndTime() - june.get(i).getStartTime();
            junetime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            junelabel.add(activity);
        }

        for (int j = 0; j < junelabel.size(); j++) {
            junelabels[j] = junelabel.get(j);
        }

        barDataSet.setStackLabels(junelabels);
        barEntries.add(new BarEntry(2, junetime));

        float[] julytime = new float[july.size()];
        String[] julylabels = new String[activtyInfoMap.size()];
        ArrayList<String> julylabel = new ArrayList<>();
        for (int i = 0; i < july.size(); i++) {
            long timespent = july.get(i).getEndTime() - july.get(i).getStartTime();
            julytime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            julylabel.add(activity);
        }

        for (int j = 0; j < julylabel.size(); j++) {
            julylabels[j] = julylabel.get(j);
        }

        barDataSet.setStackLabels(julylabels);
        barEntries.add(new BarEntry(2, julytime));

        float[] augtime = new float[august.size()];
        String[] auglabels = new String[activtyInfoMap.size()];
        ArrayList<String> auglabel = new ArrayList<>();
        for (int i = 0; i < august.size(); i++) {
            long timespent = august.get(i).getEndTime() - august.get(i).getStartTime();
            augtime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            auglabel.add(activity);
        }

        for (int j = 0; j < auglabel.size(); j++) {
            auglabels[j] = auglabel.get(j);
        }

        barDataSet.setStackLabels(auglabels);
        barEntries.add(new BarEntry(2, augtime));

        float[] septtime = new float[september.size()];
        String[] septlabels = new String[activtyInfoMap.size()];
        ArrayList<String> septlabel = new ArrayList<>();
        for (int i = 0; i < september.size(); i++) {
            long timespent = september.get(i).getEndTime() - september.get(i).getStartTime();
            septtime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            septlabel.add(activity);
        }

        for (int j = 0; j < septlabel.size(); j++) {
            septlabels[j] = septlabel.get(j);
        }

        barDataSet.setStackLabels(septlabels);
        barEntries.add(new BarEntry(2, septtime));

        float[] octtime = new float[october.size()];
        String[] octlabels = new String[activtyInfoMap.size()];
        ArrayList<String> octlabel = new ArrayList<>();
        for (int i = 0; i < october.size(); i++) {
            long timespent = october.get(i).getEndTime() - october.get(i).getStartTime();
            octtime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            octlabel.add(activity);
        }

        for (int j = 0; j < octlabel.size(); j++) {
            octlabels[j] = octlabel.get(j);
        }

        barDataSet.setStackLabels(octlabels);
        barEntries.add(new BarEntry(2, octtime));

        float[] novtime = new float[november.size()];
        String[] novlabels = new String[activtyInfoMap.size()];
        ArrayList<String> novlabel = new ArrayList<>();
        for (int i = 0; i < november.size(); i++) {
            long timespent = november.get(i).getEndTime() - november.get(i).getStartTime();
            novtime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            novlabel.add(activity);
        }

        for (int j = 0; j < novlabel.size(); j++) {
            novlabels[j] = novlabel.get(j);
        }

        barDataSet.setStackLabels(novlabels);
        barEntries.add(new BarEntry(2, novtime));

        float[] dectime = new float[december.size()];
        String[] declabels = new String[activtyInfoMap.size()];
        ArrayList<String> declabel = new ArrayList<>();
        for (int i = 0; i < december.size(); i++) {
            long timespent = december.get(i).getEndTime() - december.get(i).getStartTime();
            dectime[i] = timespent;
        }

        for (String activity : activtyInfoMap.keySet()) {
            declabel.add(activity);
        }

        for (int j = 0; j < declabel.size(); j++) {
            declabels[j] = declabel.get(j);
        }

        barDataSet.setStackLabels(declabels);
        barEntries.add(new BarEntry(2, dectime));
    }

    private void getData() {

        barEntries.add(new BarEntry(4, new float[]{2, 5.5f, 4}));
        barEntries.add(new BarEntry(5, new float[]{3, 4.3f, 4}));
        barEntries.add(new BarEntry(6, new float[]{4, 2.3f, 4}));
        barEntries.add(new BarEntry(7, new float[]{1, 5.6f, 4}));
        barEntries.add(new BarEntry(8, new float[]{2, 2.4f, 4}));

    }
}
