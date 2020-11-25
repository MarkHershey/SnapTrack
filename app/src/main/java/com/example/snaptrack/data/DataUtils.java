package com.example.snaptrack.data;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class DataUtils {

    public static final int GENERATE_ID_TRIES = 100;

    public static void createExampleUser(){
        UserInfo.add("Example username");
        CategoryInfo.add("work", "#FF8888");
        CategoryInfo.add("life", "#FF72A2");
        UserActivityInfo.add("eat", Arrays.asList("life"), "#88FF88");
        UserActivityInfo.add("sleep", Arrays.asList("life"), "#8888FF");
        UserActivityInfo.add("shitpost", Arrays.asList("work"), "#FFAA77");
    }

    /**
     * Generates a random ID for NFC tags, guaranteed to be 12 characters.
     * Not guaranteed to be unique,
     * @return a 12 character ID.
     */
    public static String generateIdForNFC(){
        String base64 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-=";
        char[] s = new char[12];
        Random r = new Random(new Date().getTime());
        for(int i = 0; i < 12; ++i){
            s[i] = base64.charAt(r.nextInt(64));
        }
        return new String(s);
    }

    /**
     * Gets Auth UID from current user.
     * @return Auth UID (a string).
     */
    public static String getAuthID(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        if (user == null){
            Log.e("Utils", "getAuthID: User not logged in!");
            throw new RuntimeException("User not logged in");
        } else {
            return user.getUid();
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
