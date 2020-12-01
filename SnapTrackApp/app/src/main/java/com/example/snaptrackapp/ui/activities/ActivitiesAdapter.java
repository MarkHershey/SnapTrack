package com.example.snaptrackapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snaptrackapp.R;
import com.example.snaptrackapp.data.UserActivityInfo;

import java.util.ArrayList;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder> {
    private static final String TAG = "ActivitiesAdapter";
    private ArrayList<UserActivityInfo> mActivityList;
    Context context;


    public ActivitiesAdapter(ArrayList<UserActivityInfo> activityList, Context context){
        mActivityList = activityList;
        this.context = context;
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        public TextView aTextView;
        public TextView cTextView;
        public CardView mCardView;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            aTextView = itemView.findViewById(R.id.activityNameTextView);
            cTextView = itemView.findViewById(R.id.categoryTextView);
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
        holder.aTextView.setText(currentItem.getActivityName());
        holder.cTextView.setText(currentItem.getCategory());
        holder.mCardView.setCardBackgroundColor(currentItem.getColor());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, currentItem.getActivityName(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Card item '" + currentItem.getActivityName() + "' is clicked");
                Intent intent = new Intent(context, EditUserActivity.class);
                intent.putExtra("activityName", currentItem.getActivityName());
                intent.putExtra("category", currentItem.getCategory());
                intent.putExtra("color", currentItem.getColor());
                // TODO: need to add AID into intent
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mActivityList.size();
    }






}
