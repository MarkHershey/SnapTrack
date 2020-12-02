package com.example.snaptrackapp.data;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DataPopulate {

    public static void addDummyUserActivity() {
        try {
            UserActivityInfo.add("Paper Reading", "Study", "#F2FF49");
            TimeUnit.MICROSECONDS.sleep(500);
            UserActivityInfo.add("Gaming", "Entertainment", "#FF4242");
            TimeUnit.MICROSECONDS.sleep(500);
            UserActivityInfo.add("Gym", "Life", "#FB62F6");
            TimeUnit.MICROSECONDS.sleep(500);
            UserActivityInfo.add("Coding Practice", "Study", "#645DD7");
            TimeUnit.MICROSECONDS.sleep(500);
            UserActivityInfo.add("Algorithm", "Study", "#B3FFFC");
            TimeUnit.MICROSECONDS.sleep(500);
            UserActivityInfo.add("HASS Reading", "Study", "#F05D5E");
            TimeUnit.MICROSECONDS.sleep(500);
            UserActivityInfo.add("Watching TV", "Entertainment", "#0F7173");
            TimeUnit.MICROSECONDS.sleep(500);
            UserActivityInfo.add("Dinner", "Life", "#E7ECEF");
            TimeUnit.MICROSECONDS.sleep(500);
            UserActivityInfo.add("Lunch", "Life", "#114B5F");
            TimeUnit.MICROSECONDS.sleep(500);
            UserActivityInfo.add("Sleep", "Life", "#D8A47F");
            TimeUnit.MICROSECONDS.sleep(500);
            UserActivityInfo.add("Bowling", "Entertainment", "#028090");
            TimeUnit.MICROSECONDS.sleep(500);
            UserActivityInfo.add("Mathematics", "Study", "#E4FDE1");
            TimeUnit.MICROSECONDS.sleep(500);
            UserActivityInfo.add("Movie", "Life", "#456990");
            TimeUnit.MICROSECONDS.sleep(500);
            UserActivityInfo.add("Meditation", "Life", "#F45B69");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addDummyEvents() {
        DataUtils.fetchActivitiesSingle(new Listener<Map<String, UserActivityInfo>>(){

            @Override
            public void update(Map<String, UserActivityInfo> stringUserActivityInfoMap) {
                if (stringUserActivityInfoMap != null) {
                    ArrayList<String> aidList = new ArrayList<>(stringUserActivityInfoMap.keySet());
                    for (String aid: aidList) {
                        try {
                            Date sTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2020-12-02 13:10:00");
                            Date eTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2020-12-02 14:45:00");
                            EventInfo info = new EventInfo(aid, sTime, eTime);
                            EventInfo.add(info);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
