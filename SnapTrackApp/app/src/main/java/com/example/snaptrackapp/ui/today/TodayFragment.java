package com.example.snaptrackapp.ui.today;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snaptrackapp.R;
import com.example.snaptrackapp.data.DataUtils;
import com.example.snaptrackapp.data.EventInfo;
import com.example.snaptrackapp.data.Listener;
import com.example.snaptrackapp.data.UserActivityInfo;
import com.example.snaptrackapp.ui.activities.ActivitiesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;


public class TodayFragment extends Fragment {

    private final String TAG = "TodayFragment";
    private TodayViewModel todayViewModel;

    private RecyclerView mRecyclerView;
    private EventsAdapter mRecyclerViewAdapter;
    private ArrayList<EventInfo> eventList;

    ArrayList<UserActivityInfo> uActivityList = new ArrayList<>();
    ArrayList<String> uActivityNamesList = new ArrayList<>();
    ArrayList<String> uActivityIDList = new ArrayList<>();

    FloatingActionButton mFloatingActionButton;
    ImageView emptyStateView;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        todayViewModel = new ViewModelProvider(this).get(TodayViewModel.class);
        View root = inflater.inflate(R.layout.fragment_today, container, false);
        // get empty state image view object
        emptyStateView = root.findViewById(R.id.todayEmpty);

        eventList = new ArrayList<EventInfo>();

        // scrolling RecyclerView stuff
        mRecyclerView = root.findViewById(R.id.eventsRecyclerView);
        // set empty adapter to avoid exception
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new EventsAdapter(eventList, getContext()));

        todayViewModel.getEventListLive().observe(getViewLifecycleOwner(), new Observer<ArrayList<EventInfo>>() {
            @Override
            public void onChanged(ArrayList<EventInfo> eventInfos) {
                if (eventInfos.size() != 0) emptyStateView.setVisibility(View.INVISIBLE);
                else emptyStateView.setVisibility(View.VISIBLE);
                mRecyclerViewAdapter = new EventsAdapter(eventInfos, getContext());
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
            }
        });

        // prepare activity list
        DataUtils.fetchActivitiesSingle(new Listener<Map<String, UserActivityInfo>>() {
            @Override
            public void update(Map<String, UserActivityInfo> stringUserActivityInfoMap) {
                uActivityList.addAll(stringUserActivityInfoMap.values());
                uActivityIDList.addAll(stringUserActivityInfoMap.keySet());
                for (String aid: uActivityIDList) {
                    uActivityNamesList.add(stringUserActivityInfoMap.get(aid).getActivityName());
                }
            }
        });

        // Floating Button stuff
        mFloatingActionButton = root.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getContext(), "Start new event", Toast.LENGTH_SHORT).show();
                // TODO: start new event tracking manually
                AlertDialog.Builder activitySelector = new AlertDialog.Builder(getContext());
                activitySelector.setTitle("Select Activity to Start");
                activitySelector.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                // init ListAdapter
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
                arrayAdapter.addAll(uActivityNamesList);
                // set ListAdapter and OnClickListener
                activitySelector.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String thisAID = uActivityIDList.get(which);
                        String thisActivityName = uActivityNamesList.get(which);
                        Toast.makeText(getContext(), "Start Tracking: " + thisActivityName, Toast.LENGTH_SHORT).show();
                        // TODO: Start Timer
                    }
                });
                // Show Alert Dialog
                activitySelector.show();
            }
        });

        return root;
    }
}