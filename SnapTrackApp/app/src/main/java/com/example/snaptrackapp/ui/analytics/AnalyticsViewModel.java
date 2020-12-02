package com.example.snaptrackapp.ui.analytics;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.snaptrackapp.data.Analytics;
import com.example.snaptrackapp.data.DataUtils;
import com.example.snaptrackapp.data.Listener;
import com.example.snaptrackapp.data.UserActivityInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AnalyticsViewModel extends ViewModel {

    private final String TAG = "AnalyticsViewModel";

    int[][] values;
    private final MutableLiveData<int[][]> analyticsDataLive;

    ArrayList<UserActivityInfo> activityList = new ArrayList<>();


    public ArrayList<String> names = new ArrayList<>();
    private final MutableLiveData<ArrayList<String>> nameDataLive;

    public ArrayList<String> AIDs = new ArrayList<>();
    private final MutableLiveData<ArrayList<String>> aidDataLive;

    public ArrayList<Integer> colors = new ArrayList<>();
    private final MutableLiveData<ArrayList<Integer>> colorDataLive;





    public AnalyticsViewModel() {
        analyticsDataLive = new MutableLiveData<>();
        nameDataLive = new MutableLiveData<>();
        aidDataLive = new MutableLiveData<>();
        colorDataLive = new MutableLiveData<>();

//        DataUtils.fetchActivities(new Listener<Map<String, UserActivityInfo>>() {
//            @Override
//            public void update(Map<String, UserActivityInfo> stringUserActivityInfoMap) {
//                activityList.clear();
//                activityList.addAll(stringUserActivityInfoMap.values());
//                names.clear();
//                AIDs.clear();
//                colors.clear();
//
//                for (UserActivityInfo activity: activityList) {
//                    names.add(activity.getActivityName());
//                    AIDs.add(activity.getAID());
//                    colors.add(activity.getColor());
//                    Log.d(TAG, activity.getActivityName());
//                }
//
//                nameDataLive.setValue(names);
//                aidDataLive.setValue(AIDs);
//                colorDataLive.setValue(colors);
//
//            }
//        });

        Analytics.fetchTotalTimesPastDays(4, new Listener<List<Map<String, Long>>>() {
            @Override
            public void update(List<Map<String, Long>> maps) {
                if (maps.size() == 0) return;

                DataUtils.fetchActivitiesSingle(new Listener<Map<String, UserActivityInfo>>() {
                    @Override
                    public void update(Map<String, UserActivityInfo> stringUserActivityInfoMap) {
                        activityList.clear();
                        activityList.addAll(stringUserActivityInfoMap.values());
                        names.clear();
                        AIDs.clear();
                        colors.clear();

                        for (UserActivityInfo activity: activityList) {
                            names.add(activity.getActivityName());
                            AIDs.add(activity.getAID());
                            colors.add(activity.getColor());
                            Log.d(TAG, activity.getActivityName());
                        }

                        nameDataLive.setValue(names);
                        aidDataLive.setValue(AIDs);
                        colorDataLive.setValue(colors);


                        values = new int[maps.size()][names.size()];
                        // init all to zero
                        for (int[] x : values){
                            Arrays.fill(x, 0);
                        }

                        for (int i = 0; i < maps.size(); i++){
                            Map<String, Long> map = maps.get(i);


                            for (Map.Entry<String,Long> entry : map.entrySet()){
                                Log.d(TAG, "Key = " + entry.getKey() + ", Value = " + entry.getValue());
                                int j = AIDs.indexOf((String) entry.getKey());
                                if (j == -1) {
                                    Log.e(TAG, "Length of AIDs" + AIDs.size());
                                    Log.e(TAG, "Key not found: "+ entry.getKey());
                                }
                                values[i][j] += (int) (entry.getValue() / 1000);
                            }
                        }
                        // update live values
                        analyticsDataLive.setValue(values);


                    }
                });







//                values = new int[maps.size()][names.size()];
//                // init all to zero
//                for (int[] x : values){
//                    Arrays.fill(x, 0);
//                }
//
//                for (int i = 0; i < maps.size(); i++){
//                    Map<String, Long> map = maps.get(i);
//
//
//                    for (Map.Entry<String,Long> entry : map.entrySet()){
//                        Log.d(TAG, "Key = " + entry.getKey() + ", Value = " + entry.getValue());
//                        int j = AIDs.indexOf((String) entry.getKey());
//                        Log.w(TAG, "Length of AIDs" + AIDs.size());
//                        if (j == -1) {
//
//                            Log.w(TAG, "Length of AIDs" + AIDs.size());
//                            Log.w(TAG, entry.getKey());
//                        }
//                        values[i][j] += (int) (entry.getValue() / 1000);
//                    }
//                }
//                // update live values
//                analyticsDataLive.setValue(values);
            }
        });

    }

    public MutableLiveData<int[][]> getAnalyticsDataLive() {
        return analyticsDataLive;
    }

    public MutableLiveData<ArrayList<Integer>> getColorDataLive() {
        return colorDataLive;
    }

    public MutableLiveData<ArrayList<String>> getAidDataLive() {
        return aidDataLive;
    }

    public MutableLiveData<ArrayList<String>> getNameDataLive() {
        return nameDataLive;
    }
}