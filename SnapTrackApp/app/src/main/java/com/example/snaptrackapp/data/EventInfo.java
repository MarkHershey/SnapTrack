package com.example.snaptrackapp.data;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;

import java.util.Date;

public class EventInfo {
    private String user_activity_id;
    private long start_time;
    private long end_time;

    /**
     * Required for DataSnapshot.getValue()
     */
    public EventInfo(){
    }

    public static void add(EventInfo info){
        String authID = DataUtils.getAuthID();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        dbref.child("users").child(authID).child("events").push().setValue(info);
    }

    public EventInfo(String user_activity_id, long start_time, long end_time) {
        this.user_activity_id = user_activity_id;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public EventInfo(String user_activity_id, Date start_time, Date end_time) {
        this(user_activity_id, start_time.getTime(), end_time.getTime());
    }

    @PropertyName("user_activity_id")
    public String getUserActivityId() {
        return user_activity_id;
    }

    @PropertyName("start_time")
    public long getStartTime() {
        return start_time;
    }

    @PropertyName("end_time")
    public long getEndTime() {
        return end_time;
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