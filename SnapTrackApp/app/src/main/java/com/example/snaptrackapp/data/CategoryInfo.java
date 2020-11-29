package com.example.snaptrackapp.data;

import android.graphics.Color;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CategoryInfo {
    private int color;

    public static void add(String categoryName, String color) {
        add(categoryName, Color.parseColor(color));
    }

    public static void add(String categoryName, int color) {
        CategoryInfo info = new CategoryInfo();
        info.color = color;
        String authID = DataUtils.getCurrentUserAuthID();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users").child(authID);
        dbref = dbref.child("categories").child(categoryName);
        dbref.setValue(info);
    }

    public int getColor() {
        return color;
    }
}