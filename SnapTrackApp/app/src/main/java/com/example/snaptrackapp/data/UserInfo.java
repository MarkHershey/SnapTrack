package com.example.snaptrackapp.data;


import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;


@IgnoreExtraProperties
public class UserInfo {

    private static final String TAG = "UserInfo";
    private String display_name;
    private String nfc_uid;

    /**
     * Required for DataSnapshot.getValue(UserInfo.class) to work.
     */
    public UserInfo(){ }

    public UserInfo(String display_name, String nfc_uid){
        this.display_name=display_name;
        this.nfc_uid=nfc_uid;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getNfc_uid() {
        return nfc_uid;
    }

    /**
     * Initialises user info on firebase.
     * You will still need to fetch the data manually to access the info.
     * @param display_name the user's display name.
     */
    public static void add(String display_name){
        add(DataUtils.GENERATE_ID_TRIES, display_name);
    }


    private static void add(int tries, String display_name){
        // generate UID
        String nfc_id = DataUtils.generateIdForNFC();

        DatabaseReference nfcIdsRef = FirebaseDatabase.getInstance().getReference("nfc_uids").child(nfc_id);
        nfcIdsRef.setValue(true, new InsertUserInfoOrRetry(tries, new UserInfo(display_name, nfc_id)));
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
            if (error == null) { // we did it

                // commit user
                DatabaseReference userInfoRef = db.getReference("users").child(DataUtils.getAuthID());
                userInfoRef.child("display_name").setValue(userInfo.display_name);
                userInfoRef.child("nfc_uid").setValue(userInfo.nfc_uid);
                Log.d(TAG, "Added New User");

            } else { // generate a new nfc tag and retry
                if(TRIES > 1){
                    add(TRIES-1, userInfo.display_name);
                } else {
                    Log.e("UserInfo","add, couldn't insert nfc_uid");
                }
            }
        }
    }

    public static void createUIDIfNotExist(String AuthID, String name) {

        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("users").child(AuthID);

        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Got snapshot");
                if (snapshot.exists()){
                    Log.d(TAG, "Existing User Logged In");
                } else {
                    Log.d(TAG, "New User Logged In");
                    UserInfo.add(name);
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