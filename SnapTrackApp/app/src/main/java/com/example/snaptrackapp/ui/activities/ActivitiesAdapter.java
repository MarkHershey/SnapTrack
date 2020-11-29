package com.example.snaptrackapp.ui.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snaptrackapp.R;
import com.example.snaptrackapp.data.UserActivityInfo;

import java.util.ArrayList;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder> {
    private ArrayList<UserActivityInfo> mActivityList;


    public ActivitiesAdapter(ArrayList<UserActivityInfo> activityList){
        mActivityList = activityList;
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public CardView mCardView;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.activityNameTextView);
            mCardView = itemView.findViewById(R.id.cardView);
        }
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        ActivityViewHolder aViewHolder = new ActivityViewHolder(v);
        return aViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        UserActivityInfo currentItem = mActivityList.get(position);
        holder.mTextView.setText(currentItem.getActivityName());
        holder.mCardView.setCardBackgroundColor(currentItem.getColor());

    }

    @Override
    public int getItemCount() {
        return mActivityList.size();
    }






}
