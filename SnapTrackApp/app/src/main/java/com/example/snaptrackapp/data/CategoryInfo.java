package com.example.snaptrackapp.data;

import android.graphics.Color;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CategoryInfo {
    public static final String ALL_CATEGORY_PARENT = "categories";
    private int color;

    public static void add(String categoryName, String color) {
        add(categoryName, Color.parseColor(color));
    }

    public static void add(String categoryName, int color) throws IllegalArgumentException {
        for (Character c : "./[]$#".toCharArray()){
            if (categoryName.indexOf(c) >= 0){
                throw new IllegalArgumentException("Illegal key name given for Firebase Database");
            }
        }


        CategoryInfo info = new CategoryInfo();
        info.color = color;
        String authID = DataUtils.getCurrentUserAuthID();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(UserInfo.ALL_USER_PARENT).child(authID);
        dbRef = dbRef.child(ALL_CATEGORY_PARENT).child(categoryName);
        dbRef.setValue(info);
    }

    public int getColor() {
        return color;
    }
}