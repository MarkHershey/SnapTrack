package com.example.snaptrackapp.ui.today;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snaptrackapp.R;

public class TodayHold extends RecyclerView.ViewHolder {

    static TextView namee;
    static TextView timee;


    public TodayHold(@NonNull View itemView) {
        super(itemView);

        this.namee = itemView.findViewById(R.id.text_name1);
        this.timee = itemView.findViewById(R.id.text_time1);
    }

}
