package com.example.snaptrackapp;

import java.util.ArrayList;

public class UserActivity {
    String AID;
    String activity_name;
    String short_display_name;
    String color;
    ArrayList<String> labels;


    public UserActivity(String AID, String name, String short_name, String color){
        this.AID = AID;
        this.activity_name = name;
        this.short_display_name = short_name;
        this.color = color;
    }

    public String getActivity_name(){
        return this.activity_name;
    }

    public String getColor(){
        return this.color;
    }
}
