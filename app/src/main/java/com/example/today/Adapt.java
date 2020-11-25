package com.example.today;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapt extends RecyclerView.Adapter<Hold> {

    Context c;

    ArrayList<Model> models; // this array list create a list of array which parameters define in model.java

    public Adapt(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
    }


    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,null);
        //this line above inflate the row

        return new Hold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {
        Hold.namee.setText(models.get(position).getName());
        Hold.timee.setText(models.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
