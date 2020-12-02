package com.example.snaptrackapp.data;


import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;

import java.util.Date;
import java.util.Map;

public class EventInfo {
    public static final String ALL_EVENT_PARENT = "events";
    private static final String TAG = "EventInfo";
    private String user_activity_id;
    public String user_activity_name;
    public int user_activity_color;
    private long start_time;
    private long end_time;
    private int duration_seconds;

    /**
     * Required for DataSnapshot.getValue()
     */
    public EventInfo(){
    }

    public static void add(EventInfo info){

        // fetch the list of User Activities to check if specified activity exists
        DataUtils.fetchActivitiesSingle(new Listener<Map<String, UserActivityInfo>>() {
            @Override
            public void update(Map<String, UserActivityInfo> stringUserActivityInfoMap) {
                if (stringUserActivityInfoMap.containsKey(info.getUserActivityId())) {
                    // activity exists
                    // get activity name
                    UserActivityInfo thisActivityInfo = stringUserActivityInfoMap.get(info.getUserActivityId());
                    info.user_activity_name = thisActivityInfo.getActivityName();
                    // TODO to get color
                    // add to firebase
                    String authID = DataUtils.getCurrentUserAuthID();
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                    dbRef.child(UserInfo.ALL_USER_PARENT).child(authID).child(ALL_EVENT_PARENT).push().setValue(info);
                } else {
                    // activity does not exist
                    Log.e(TAG, "Failed to add Event because specified AID does not exist in Firebase.");
                }
            }
        });


    }

    public EventInfo(String user_activity_id, long start_time, long end_time) {
        this.user_activity_id = user_activity_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.duration_seconds = (int) (end_time - start_time) / 1000;
        this.user_activity_name = null;

    }

    public EventInfo(String user_activity_id, Date start_time, Date end_time) {
        this(user_activity_id, start_time.getTime(), end_time.getTime());
    }

    @PropertyName("user_activity_id")
    public String getUserActivityId() {
        return user_activity_id;
    }

    @PropertyName("user_activity_name")
    public String getUserActivityName() {
        return user_activity_name;
    }

    @PropertyName("start_time")
    public long getStartTime() {
        return start_time;
    }

    @PropertyName("end_time")
    public long getEndTime() {
        return end_time;
    }

    @PropertyName("duration_seconds")
    public int getDurationSeconds() {
        return duration_seconds;
    }

    @Exclude
    public Date getStartTimeAsDateTime(){
        return new Date(start_time);
    }

    @Exclude
    public Date getEndTimeAsDateTime(){
        return new Date(end_time);
    }
}
