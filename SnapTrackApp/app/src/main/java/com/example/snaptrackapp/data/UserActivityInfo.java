package com.example.snaptrackapp.data;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserActivityInfo {

    public static final String ALL_USER_ACTIVITY_PARENT = "activities";
    private final static String TAG = "UserActivityInfo";

    private String activity_name;
    private String category;
    private int color;

    /**
     * Required for DataSnapshot.getValue()
     */
    public UserActivityInfo(){}

    public UserActivityInfo(String activity_name, String category, int color) {
        this.activity_name = activity_name;
        this.category = category;
        this.color = color;
    }

    @PropertyName("activity_name")
    public String getActivityName() {
        return this.activity_name;
    }

    @PropertyName("category")
    public String getCategory() {
        return this.category;
    }

    @PropertyName("color")
    public int getColor() {
        return color;
    }

    public static void add(String activityName, String category, String color) {
        add(activityName, category, Color.parseColor(color));
    }

    public static void add(String activityName, String category, int color) {
        add(activityName, category, color, DataUtils.GENERATE_ID_TRIES);
    }

    private static void add(String activityName, String category, int color, int tries){

        String authID = DataUtils.getCurrentUserAuthID();
        String AID = DataUtils.generateRandomID();
        Log.d(TAG, "New AID generated: " + AID);

        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference(UserInfo.ALL_USER_PARENT)
                .child(authID)
                .child("activityNames")
                .child(activityName);

        UserActivityInfo info = new UserActivityInfo(activityName, category, color);
        dbRef.addListenerForSingleValueEvent(new InsertUserActivityOrRetry(tries, AID, info));

    }

    /**
     * Helper private class for add.
     * If value doesn't exist, store it in the value, otherwise retry.
     * What happens if the same person tries to insert two of the same nfc_id at once?
     * Probably a good time to buy 4d.
     */
    private static class InsertUserActivityOrRetry implements ValueEventListener {
        private final int TRIES;
        private final String AID;
        private final UserActivityInfo userActivityInfo;

        private InsertUserActivityOrRetry(int tries, String AID, UserActivityInfo userActivityInfo) {
            TRIES = tries;
            this.AID = AID;
            this.userActivityInfo = userActivityInfo;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if (!snapshot.exists()){
                String authID = DataUtils.getCurrentUserAuthID();
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                dbRef = dbRef
                        .child(UserInfo.ALL_USER_PARENT)
                        .child(authID)
                        .child(ALL_USER_ACTIVITY_PARENT)
                        .child(AID);
                dbRef.setValue(userActivityInfo);

                dbRef = FirebaseDatabase.getInstance()
                        .getReference(UserInfo.ALL_USER_PARENT)
                        .child(authID)
                        .child("activityNames")
                        .child(userActivityInfo.getActivityName());
                dbRef.setValue(true);

            } else if (TRIES > 0) {
                Log.w(TAG, "trying another time");
                add(userActivityInfo.activity_name, userActivityInfo.category, userActivityInfo.color, TRIES-1);

            } else {
                Log.e(TAG, "Failed to add new activity to Firebase: " + userActivityInfo.activity_name);

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    }









}