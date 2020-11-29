package com.example.snaptrackapp.data;


import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ValueEventListener;


public class UserInfo {

    public static final String ALL_USER_PARENT = "users";
    private static final String TAG = "UserInfo";
    private String userName;
    private String userID;
    private String authID;

    /**
     * Required for DataSnapshot.getValue(UserInfo.class) to work.
     */
    public UserInfo(){ }

    public UserInfo(String userName, String userID, String authID){
        this.userName=userName;
        this.userID=userID;
        this.authID=authID;
    }

    @PropertyName("userName")
    public String getUserName() {
        return userName;
    }

    @PropertyName("userID")
    public String getUserID() {
        return userID;
    }

    @PropertyName("authID")
    public String getAuthID() {
        return authID;
    }

    /**
     * Initialises user info on firebase.
     * You will still need to fetch the data manually to access the info.
     * @param userName the user's display name.
     */
    public static void add(String userName, String authID){
        add(DataUtils.GENERATE_ID_TRIES, userName, authID);
    }


    private static void add(int tries, String userName, String authID){
        // generate userID
        String userID = DataUtils.generateRandomID();

        DatabaseReference nfcIdsRef = FirebaseDatabase.getInstance().getReference("userIDs").child(userID);
        nfcIdsRef.setValue(true, new InsertUserInfoOrRetry(tries, new UserInfo(userName, userID, authID)));
    }


    /**
     * Helper private class for add.
     * Used to ensure NFC UID generated is unique before we store user info.
     * Stores user info on completion and tries again (if tries remaining > 1)
     * with a different UID on failure. Ideally shouldn't need more than 1 try,
     * since failure chance is really small.
     */
    private static class InsertUserInfoOrRetry implements DatabaseReference.CompletionListener {
        private final int TRIES;
        private final UserInfo userInfo;

        public InsertUserInfoOrRetry(int TRIES, UserInfo userInfo) {
            this.TRIES = TRIES;
            this.userInfo = userInfo;
        }
        @Override
        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            if (error == null) {

                // commit user
                DatabaseReference userInfoRef = db.getReference(ALL_USER_PARENT).child(DataUtils.getCurrentUserAuthID());
                userInfoRef.child("userName").setValue(userInfo.getUserName());
                userInfoRef.child("userID").setValue(userInfo.getUserID());
                userInfoRef.child("authID").setValue(userInfo.getAuthID());

                Log.d(TAG, "Added New User");

            } else {
                if(TRIES > 1){
                    add(TRIES-1, userInfo.userName, userInfo.authID);
                } else {
                    Log.e(TAG,"add, couldn't insert userID");
                }
            }
        }
    }

    /**
     * This static function checks with Firebase if the user already has a record in the database.
     * If yes, this is an existing user, do nothing.
     * If no, this is a newly signed up user, we initialise this user in Firebase.
     *
     * @param userName Name of the User
     * @param authID Firebase-generated UID of the User
     */
    public static void addUserInfoIfNotExist(String userName, String authID) {

        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference(ALL_USER_PARENT).child(authID);

        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Got snapshot");
                if (snapshot.exists()){
                    Log.d(TAG, "Existing User Logged In");
                } else {
                    Log.d(TAG, "New User Logged In");
                    // create user and userID
                    UserInfo.add(userName, authID);
                    // create default categories for all user
                    CategoryInfo.add("Work", "#FF8888");
                    CategoryInfo.add("Life", "#FF72A2");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed getting snapshot");
                String errorMessage = error.getMessage();
                Log.e(TAG, errorMessage);
            }
        });
    }










}
