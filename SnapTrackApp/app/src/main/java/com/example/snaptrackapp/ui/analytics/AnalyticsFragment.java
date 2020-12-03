package com.example.snaptrackapp.ui.analytics;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.snaptrackapp.R;
import com.example.snaptrackapp.data.DataUtils;
import com.example.snaptrackapp.data.UserActivityInfo;
import com.example.snaptrackapp.data.UserInfo;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class AnalyticsFragment extends Fragment {

    private static final String TAG="AnalyticsFragment";

    private AnalyticsViewModel analyticsViewModel;
    ArrayList<String> aids;
    ArrayList<Integer> colors;
    ArrayList<String> names;


    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList<BarEntry> barEntries = new ArrayList<>();

    ImageView emptyStateView;


    public static AnalyticsFragment newInstance() {
        return new AnalyticsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_analytics, container, false);
        barChart = root.findViewById(R.id.barChart);
        emptyStateView = root.findViewById(R.id.analyticsEmpty);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        analyticsViewModel = new ViewModelProvider(this).get(AnalyticsViewModel.class);
        // TODO: Use the ViewModel

//        analyticsViewModel.getAidDataLive().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
//            @Override
//            public void onChanged(ArrayList<String> strings) {
//                //TODO
//                aids = new ArrayList<>(strings);
//            }
//        });
//
//        analyticsViewModel.getColorDataLive().observe(getViewLifecycleOwner(), new Observer<ArrayList<Integer>>() {
//            @Override
//            public void onChanged(ArrayList<Integer> integers) {
//                //TODO
//                colors = new ArrayList<>(integers);
//
//            }
//        });
//
//        analyticsViewModel.getNameDataLive().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
//            @Override
//            public void onChanged(ArrayList<String> strings) {
//                //TODO
//                names = new ArrayList<>(strings);
//            }
//        });


        // values
        analyticsViewModel.getAnalyticsDataLive().observe(getViewLifecycleOwner(), new Observer<int[][]>() {
            @Override
            public void onChanged(int[][] ints) {
                // init bar chart
                // Log.d(TAG, "setMaxVisibleValueCount: " + ints.length);
                barChart.setMaxVisibleValueCount(ints.length);
                barChart.getDescription().setEnabled(false);
                barChart.getLegend().setTextColor(Color.WHITE);
                barChart.getXAxis().setTextColor(Color.WHITE);
                barChart.getAxisLeft().setTextColor(Color.WHITE);
                barChart.getAxisRight().setTextColor(Color.WHITE);

                int counter = 1;

                for (int[] y:ints) {
                    // cast all int to float
                    float[] v = new float[y.length];
                    for (int i=0; i<y.length; i++){
                        v[i] = (float) y[i];
                    }
                    // add bar entries
                    // Log.d(TAG, "add bar entries: " + Arrays.toString(v));
                    barEntries.add(new BarEntry(counter, v));
                    counter++;
                }


                if (analyticsViewModel.names != null && analyticsViewModel.AIDs != null) {
                    String[] labels = new String[analyticsViewModel.names.size()];
                    for (int i = 0; i < analyticsViewModel.names.size(); i++) {
                        labels[i] = (String) analyticsViewModel.names.get(i);
                    }

                    barDataSet = new BarDataSet(barEntries, "daily");
                    // Log.d(TAG, analyticsViewModel.colors.toString());
                    barDataSet.setColors(analyticsViewModel.colors);

                    // Log.d(TAG, Arrays.toString(labels));
                    barDataSet.setStackLabels(labels);

                    barData = new BarData(barDataSet);
                    barChart.setData(barData);
                } else {
                    // Log.d(TAG, "failed");
                }
                barChart.setFitBars(true);
                emptyStateView.setVisibility(View.INVISIBLE);
                barChart.invalidate();

            }
        });

    // NOTE: previous
//        analyticsViewModel.getEventAnalyticsListLive().observe(getViewLifecycleOwner(), new Observer<ArrayList<Map<String, Long>>>() {
//            @Override
//            public void onChanged(ArrayList<Map<String, Long>> maps) {
//
//                // init bar chart
//                barChart.setMaxVisibleValueCount(maps.size());
//                barChart.getDescription().setEnabled(false);
//                barChart.getLegend().setTextColor(Color.WHITE);
//                barChart.getXAxis().setTextColor(Color.WHITE);
//                barChart.getAxisLeft().setTextColor(Color.WHITE);
//                barChart.getAxisRight().setTextColor(Color.WHITE);
//
//                // counter
//                int counter = 1;
//
//
//                // loop thru data
//                for (Map<String, Long> map:maps) {
//                    ArrayList<String> AIDs = new ArrayList<>(map.keySet());
//                    ArrayList<String> names = new ArrayList<>(map.keySet());
//                    ArrayList<Long> durations = new ArrayList<>(map.values());
//                    float[] values = new float[durations.size()];
//                    ArrayList<Integer> colors = new ArrayList<>();
//
//                    for (String AID:AIDs) {
//                        DatabaseReference dbRef = FirebaseDatabase.getInstance()
//                                .getReference(UserInfo.ALL_USER_PARENT)
//                                .child(DataUtils.getCurrentUserAuthID())
//                                .child(UserActivityInfo.ALL_USER_ACTIVITY_PARENT)
//                                .child(AID);
//
//                        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (snapshot != null) {
//                                    UserActivityInfo thisActivity = snapshot.getValue(UserActivityInfo.class);
//                                    colors.add(thisActivity.getColor());
//                                    names.add(thisActivity.getActivityName());
//                                }
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                Log.e(TAG, error.toString());
//                            }
//                        });
//                    }
//
//                    // convert into seconds in float
//                    for (int i=0; i<durations.size(); i++){
//                        values[i] = (float) (durations.get(i) / 1000);
//                    }
//
//                    // add bar entry
//                    barEntries.add(new BarEntry(counter, values));
//                    counter++;
//
//                }
//            }
//        });

        // to be removed
        // init bar chart
//        barChart.setMaxVisibleValueCount(5);
//        barChart.getDescription().setEnabled(false);
//        barChart.getLegend().setTextColor(Color.WHITE);
//        barChart.getXAxis().setTextColor(Color.WHITE);
//        barChart.getAxisLeft().setTextColor(Color.WHITE);
//        barChart.getAxisRight().setTextColor(Color.WHITE);
//        int counter = 1;
//        for (int[] y:analyticsViewModel.values) {
//            // cast all int to float
//            float[] v = new float[y.length];
//            for (int i=0; i<y.length; i++){
//                v[i] = (float) y[i];
//            }
//            // add bar entries
//            barEntries.add(new BarEntry(counter, v));
//            counter++;
//        }


    }


}
