package com.example.snaptrackapp.data;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DataPopulate {

    private static final String TAG = "DataPopulate";

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
            Log.e(TAG, e.getMessage());
        }

    }

    public static void addDummyEvents() {
        Random rand = new Random(new Date().getTime());


        DataUtils.fetchActivitiesSingle(new Listener<Map<String, UserActivityInfo>>(){

            @Override
            public void update(Map<String, UserActivityInfo> stringUserActivityInfoMap) {
                if (stringUserActivityInfoMap != null) {
                    ArrayList<String> aidList = new ArrayList<>(stringUserActivityInfoMap.keySet());
                    for (String aid: aidList) {
                        // day 1 to  2
                        for (int day=1; day<3; day++) {
                            try {
                                String baseString = "2020-12-0" + day;
                                int min = 1;
                                int max = 18;
                                int randomStartHour = rand.nextInt((max - min) + 1) + min;
                                min = 0;
                                max = 5;
                                int randomDurationHours = rand.nextInt((max - min) + 1) + min;
                                int endHour = randomStartHour + randomDurationHours;

                                String startH = String.valueOf(randomStartHour);
                                String endH = String.valueOf(endHour);

                                if (randomStartHour < 10) startH = "0" + String.valueOf(randomStartHour);
                                if (endHour < 10) endH = "0" + String.valueOf(endHour);

                                String startString = baseString + " " + startH + ":10:00";
                                String endString = baseString + " " + endH + ":30:23";

                                Date sTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startString);
                                Date eTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endString);
                                EventInfo info = new EventInfo(aid, sTime, eTime);
                                EventInfo.add(info);
                            } catch (Exception e){
                                Log.e(TAG, e.getMessage());
                            }

                        }

                        // day 26 - 30
                        for (int day=26; day<31; day++) {
                            try {
                                String baseString = "2020-11-" + day;
                                int min = 1;
                                int max = 18;
                                int randomStartHour = rand.nextInt((max - min) + 1) + min;
                                min = 0;
                                max = 5;
                                int randomDurationHours = rand.nextInt((max - min) + 1) + min;
                                int endHour = randomStartHour + randomDurationHours;

                                String startH = String.valueOf(randomStartHour);
                                String endH = String.valueOf(endHour);

                                if (randomStartHour < 10) startH = "0" + String.valueOf(randomStartHour);
                                if (endHour < 10) endH = "0" + String.valueOf(endHour);

                                String startString = baseString + " " + startH + ":12:00";
                                String endString = baseString + " " + endH + ":40:45";

                                Date sTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startString);
                                Date eTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endString);
                                EventInfo info = new EventInfo(aid, sTime, eTime);
                                EventInfo.add(info);
                            } catch (Exception e){
                                Log.e(TAG, e.getMessage());
                            }

                        }


                    }
                }
            }
        });
    }
}
