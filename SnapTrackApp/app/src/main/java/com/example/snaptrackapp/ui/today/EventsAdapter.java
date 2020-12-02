package com.example.snaptrackapp.ui.today;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.snaptrackapp.R;
import com.example.snaptrackapp.data.EventInfo;
import com.example.snaptrackapp.ui.activities.ActivitiesAdapter;

import java.util.ArrayList;

public class EventsAdapter  extends RecyclerView.Adapter<EventsAdapter.EventViewHolder>{
    private static final String TAG = "EventsAdapter";
    ArrayList<EventInfo> eventList;
    Context context;

    public EventsAdapter(ArrayList<EventInfo> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView activityTextView;
        public TextView durationTextView;
        public CardView mCardView;


        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            activityTextView = itemView.findViewById(R.id.activityTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            mCardView = itemView.findViewById(R.id.cardEvent);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event_item, parent, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventInfo currentItem = eventList.get(position);
        holder.activityTextView.setText(currentItem.getUserActivityName());
        holder.durationTextView.setText(String.valueOf(currentItem.getDurationSeconds()));
        // set card height according to Event duration
        int heightValue = (int) currentItem.getDurationSeconds() / 36;
        if (heightValue < 50) heightValue = 50;
        ViewGroup.LayoutParams params = holder.mCardView.getLayoutParams();
        params.height = heightValue;
        holder.mCardView.setLayoutParams(params);
        // set card color according to activity color
        holder.mCardView.setCardBackgroundColor(currentItem.getUserActivityColor());

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }









}
