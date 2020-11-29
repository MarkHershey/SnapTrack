package com.example.snaptrackapp.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DataUtils {

    public static final int GENERATE_ID_TRIES = 100;
    private static final String TAG = "DataUtils";

    public static void createExampleUser(){
        UserInfo.add("Example username", "xxxxxx");
        CategoryInfo.add("work", "#FF8888");
        CategoryInfo.add("life", "#FF72A2");
        UserActivityInfo.add("eat", "life", "#88FF88");
        UserActivityInfo.add("sleep", "life", "#8888FF");
        UserActivityInfo.add("shitpost", "work", "#FFAA77");
    }

    /**
     * Fetches the Map from Activity ID to UserActivityInfo. Calls callback.update() whenever data changes.
     * @param callback Listener for Activities map.
     */
    public static void fetchActivities(Listener<Map<String,UserActivityInfo>> callback){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("users").child(DataUtils.getCurrentUserAuthID()).child("activities");
        fetchData(callback, dbRef);
    }

    /**
     * Fetches the List of EventInfo. Calls callback.update() whenever data changes.
     * @param callback Listener for Event Info List.
     */
    public static void fetchEvents(Listener<List<EventInfo>> callback){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("users").child(DataUtils.getCurrentUserAuthID()).child("events");
        fetchData(callback, dbRef);
    }

    /**
     * Fetches the List of EventInfo within a certain time range.
     * @param callback Listener for Event Info List.
     * @param startTime Start time.
     * @param endTime End time.
     */
    public static void fetchEvents(Listener<List<EventInfo>> callback, Date startTime, Date endTime){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("users").child(DataUtils.getCurrentUserAuthID()).child("events");
        fetchData(callback, dbRef.startAt(startTime.getTime(), "start_time").endAt(endTime.getTime(), "end_time"));
    }

    /**
     * Fetches the Map from Category name to CategoryInfo. Calls callback.update() whenever data changes.
     * @param callback Listener for Category Info Map.
     */
    public static void fetchCategories(Listener<Map<String, CategoryInfo>> callback){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("users").child(DataUtils.getCurrentUserAuthID()).child("categories");
        fetchData(callback, dbRef);
    }

    private static <T> void fetchData(Listener<T> callback, Query q){
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                T t;
                GenericTypeIndicator<T> gti = new GenericTypeIndicator<T>(){};
                t = snapshot.getValue(gti);
                callback.update(t);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, String.format("Error fetching data"));
            }
        });
    }

    private static <T> void fetchData(Listener<T> callback, Query q, Class<T> c){
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                T t;
                t = snapshot.getValue(c);
                callback.update(t);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, String.format("Error fetching data"));
            }
        });
    }

    /**
     * Generates a random ID, guaranteed to be 16 characters.
     * Not guaranteed to be unique,
     * @return a 16 character ID.
     */
    public static String generateRandomID(){
        String base64 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-=";
        char[] s = new char[16];
        Random r = new Random(new Date().getTime());
        for(int i = 0; i < 16; ++i){
            s[i] = base64.charAt(r.nextInt(64));
        }
        return new String(s);
    }

    /**
     * Gets current FirebaseUser object from Firebase Authentication
     * @return userLoggedIn (FirebaseUser)
     */
    public static FirebaseUser getCurrentUser() {
        FirebaseUser userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();
        if (userLoggedIn == null){
            Log.e(TAG, "getAuthID: User not logged in!");
            throw new RuntimeException("User not logged in");
        } else {
            return userLoggedIn;
        }
    }

    /**
     * Gets Firebase-generated authID for currently logged-in user from Firebase.
     * @return authID (a string).
     */
    public static String getCurrentUserAuthID(){
        FirebaseUser userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();
        if (userLoggedIn == null){
            Log.e(TAG, "getAuthID: User not logged in!");
            throw new RuntimeException("User not logged in");
        } else {
            return userLoggedIn.getUid();
        }
    }

    /**
     * Escapes the characters ".$[]#/%_" so as to allow for use as firebase key.
     * Blatantly stolen from https://stackoverflow.com/questions/19132867/adding-firebase-data-dots-and-forward-slashes/39561350#39561350
     * @param s the string to be encoded
     * @return the encoded string
     */
    public static String encodeForFirebaseKey(String s) {
        return s
                .replace("_", "__")
                .replace(".", "_P")
                .replace("$", "_D")
                .replace("#", "_H")
                .replace("[", "_O")
                .replace("]", "_C")
                .replace("/", "_S")
                ;
    }

    /**
     * Decodes the results of encodeForFirebaseKey.
     * Blatantly stolen from https://stackoverflow.com/questions/19132867/adding-firebase-data-dots-and-forward-slashes/39561350#39561350
     * @param s the string to decode
     * @return the decoded string
     */
    public static String decodeFromFirebaseKey(String s) {
        int i = 0;
        int ni;
        String res = "";
        while ((ni = s.indexOf("_", i)) != -1) {
            res += s.substring(i, ni);
            if (ni + 1 < s.length()) {
                char nc = s.charAt(ni + 1);
                if (nc == '_') {
                    res += '_';
                } else if (nc == 'P') {
                    res += '.';
                } else if (nc == 'D') {
                    res += '$';
                } else if (nc == 'H') {
                    res += '#';
                } else if (nc == 'O') {
                    res += '[';
                } else if (nc == 'C') {
                    res += ']';
                } else if (nc == 'S') {
                    res += '/';
                } else {
                    // this case is due to bad encoding
                }
                i = ni + 2;
            } else {
                // this case is due to bad encoding
                break;
            }
        }
        res += s.substring(i);
        return res;
    }

}
