package com.example.snaptrackapp.ui.today;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snaptrackapp.R;

import java.util.ArrayList;

public class TodayAdapt extends RecyclerView.Adapter<TodayHold> {

    TodayFragment c;

    ArrayList<TodayModel> models; // this array list create a list of array which parameters define in model.java

    public TodayAdapt(TodayFragment c, ArrayList<TodayModel> models) {
        this.c = c;
        this.models = models;
    }


    @NonNull
    @Override
    public TodayHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_eventrow,null);
        //this line above inflate the row
        return new TodayHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayHold holder, int position) {
        TodayHold.namee.setText(models.get(position).getName());
        TodayHold.timee.setText(models.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}

