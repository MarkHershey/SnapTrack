package com.example.snaptrackapp.data;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserActivityInfo {
    private List<String> categories;
    private String activity_name;
    private int color;
    private final static String TAG = "UserActivityInfo";

    /**
     * Required for DataSnapshot.getValue()
     */
    public UserActivityInfo(){}

    public UserActivityInfo(List<String> categories, String activity_name, int color) {
        this.categories = categories;
        this.activity_name = activity_name;
        this.color = color;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String gatActivity_name() {
        return activity_name;
    }

    public int getColor() {
        return color;
    }

    public static void add(String activityName, List<String> categories, String color) {
        add(activityName, categories, Color.parseColor(color));
    }

    public static void add(String activityName, List<String> categories, int color) {
        add(activityName, categories, color, DataUtils.GENERATE_ID_TRIES);
    }

    private static void add(String activityName, List<String> categories, int color, int tries){
        String authID = DataUtils.getAuthID();
        String nfc_id = DataUtils.generateIdForNFC();
        DatabaseReference activities = FirebaseDatabase.getInstance().getReference();
        activities = activities.child("users").child(authID).child("activities");
        UserActivityInfo info = new UserActivityInfo(categories, activityName, color);
        activities.addListenerForSingleValueEvent(new InsertUserActivityOrRetry(tries, nfc_id, info));
    }

    /**
     * Helper private class for add.
     * If value doesn't exist, store it in the value, otherwise retry.
     * What happens if the same person tries to insert two of the same nfc_id at once?
     * Probably a good time to buy 4d.
     */
    private static class InsertUserActivityOrRetry implements ValueEventListener {
        private final int TRIES;
        private final String nfc_id;
        private final UserActivityInfo userActivityInfo;

        private InsertUserActivityOrRetry(int tries, String nfc_id, UserActivityInfo userActivityInfo) {
            TRIES = tries;
            this.nfc_id = nfc_id;
            this.userActivityInfo = userActivityInfo;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (!snapshot.exists()){
                DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();
                dbr = dbr.child("users").child(DataUtils.getAuthID()).child("activities").child(nfc_id);
                dbr.setValue(userActivityInfo);
            } else if (TRIES > 0) {
                add(userActivityInfo.activity_name, userActivityInfo.categories, userActivityInfo.color, TRIES-1);
            } else {
                Log.e(TAG, "Failed to add activity " + userActivityInfo.activity_name);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    }
}