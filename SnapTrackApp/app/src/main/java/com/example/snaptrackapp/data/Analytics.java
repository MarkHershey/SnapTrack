package com.example.snaptrackapp.data;


import java.util.*;
import java.time.*;

public class Analytics {


    public void fetchTotalTimes(Listener<Map<String, Long>> callback, long startTime, long endTime){
        DataUtils.fetchEventsSingle(new Listener<List<EventInfo>>() {
            @Override
            public void update(List<EventInfo> eventInfos) {
                HashMap<String, Long> durations = new HashMap<>();
                for (EventInfo info : eventInfos){
                    String aid = info.getUserActivityId();
                    long dur = info.getEndTime()-info.getStartTime();
                    if (!durations.containsKey(aid)){
                        durations.put(aid, dur);
                    } else {
                        durations.put(aid, dur + durations.get(aid));
                    }
                }
            }
        }, startTime, endTime);
    }

    public void fetchTotalTimesPastDays(Listener<List<Map<String, Long>>> callback,
                                     int nDays){
        final long MILLIS_IN_A_DAY = 24*60*60*1000;
        Date tomorrow = new Date(System.currentTimeMillis() + MILLIS_IN_A_DAY);
        tomorrow = new Date(tomorrow.getYear(), tomorrow.getMonth(), tomorrow.getDate());
        long startTime = tomorrow.getTime() - nDays * MILLIS_IN_A_DAY;
        long endTime = tomorrow.getTime();
        Listener<List<EventInfo>> countDailyDurations = new Listener<List<EventInfo>>() {
            @Override
            public void update(List<EventInfo> eventInfos) {
                List<Map<String, Long>> eventLists = new ArrayList<>();
                for (int i = 0; i < nDays; ++i){
                    eventLists.add(new HashMap<String, Long>());
                }
                for (EventInfo info : eventInfos){
                    String aid = info.getUserActivityId();
                    long startDay = (info.getStartTime() - startTime) / MILLIS_IN_A_DAY;
                    long endDay = (info.getEndTime() - startTime) / MILLIS_IN_A_DAY;
                    long startTimeOfDay = (info.getStartTime() - startTime) % MILLIS_IN_A_DAY;
                    long endTimeOfDay = (info.getEndTime() - startTime) % MILLIS_IN_A_DAY;

                    if (startDay == endDay){
                        Map<String, Long> day = eventLists.get((int)startDay);
                        if(day.containsKey(aid)) day.put(aid, endTimeOfDay-startTimeOfDay);
                        else day.put(aid, day.get(aid)+ endTimeOfDay-startTimeOfDay);
                    } else {
                        Map<String, Long> day = eventLists.get((int)startDay);
                        if(day.containsKey(aid)) day.put(aid, MILLIS_IN_A_DAY-startTimeOfDay);
                        else day.put(aid, day.get(aid)+ MILLIS_IN_A_DAY-startTimeOfDay);
                        day = eventLists.get((int)endDay);
                        if(day.containsKey(aid)) day.put(aid, endTimeOfDay);
                        else day.put(aid, day.get(aid) + endTimeOfDay);
                        for(long d = startDay+1; d < endDay; ++d){
                            day = eventLists.get((int)d);
                            if(day.containsKey(aid)) day.put(aid, MILLIS_IN_A_DAY);
                            else day.put(aid, day.get(aid)+ MILLIS_IN_A_DAY);
                        }
                    }
                }
                callback.update(eventLists);
            }
        };
        DataUtils.fetchEventsSingle(countDailyDurations, startTime, endTime);
    }
}
