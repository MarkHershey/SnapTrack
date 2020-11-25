package com.example.today;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Hold extends RecyclerView.ViewHolder {

    static TextView namee;
    static TextView timee;


    public Hold(@NonNull View itemView) {
        super(itemView);

        this.namee = itemView.findViewById(R.id.text_name1);
        this.timee = itemView.findViewById(R.id.text_time1);
    }


}
