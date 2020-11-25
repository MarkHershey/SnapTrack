package com.example.snaptrack.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public String getUser_activity_id() {
        return user_activity_id;
    }

    public long getStart_time() {
        return start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public Date getStartTimeAsDateTime(){
        return new Date(start_time);
    }

    public Date getEndTimeAsDateTime(){
        return new Date(end_time);
    }
}
