package com.example.snaptrackapp.ui.me;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snaptrackapp.MainActivity;
import com.example.snaptrackapp.R;

import com.example.snaptrackapp.data.CategoryInfo;
import com.example.snaptrackapp.data.DataPopulate;
import com.example.snaptrackapp.data.DataUtils;
import com.example.snaptrackapp.data.EventInfo;
import com.example.snaptrackapp.data.UserActivityInfo;
import com.example.snaptrackapp.data.UserInfo;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;


public class MeFragment extends Fragment {

    private static final String TAG = "MeFragment";
    private MeViewModel meViewModel;

    TextView userNameText;
    TextView userEmailText;
    Button resetAccountButton;
    Button signOutButton;
    Button devButton;
    Button devButton2;

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_me, container, false);

        // get text view reference
        userNameText = root.findViewById(R.id.userNameTextView);
        userEmailText = root.findViewById(R.id.userEmailTextView);
        // get button
        resetAccountButton = root.findViewById(R.id.resetAccountButton);
        signOutButton = root.findViewById(R.id.signOutButton);

        // perform reset account on click
        // the clears all user data
        resetAccountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetCurrentUser();
                Toast.makeText(getContext(), "You have reset your account", Toast.LENGTH_SHORT).show();
            }
        });


        // perform sign out on click
        signOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Logging out", Toast.LENGTH_SHORT).show();
                signOut();
            }
        });


        // NOTE: following lines are for development debugging, will be removed later
        devButton = root.findViewById(R.id.devButton);
        devButton2 = root.findViewById(R.id.devButton2);
        devButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Dev button pressed", Toast.LENGTH_SHORT).show();
                DataPopulate.addDummyUserActivity();
            }
        });
        devButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Dev button pressed", Toast.LENGTH_SHORT).show();
                DataPopulate.addDummyEvents();
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get View Model
        meViewModel = new ViewModelProvider(this).get(MeViewModel.class);

        // Update User Name Text View
        meViewModel.getUserName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                userNameText.setText(s);
            }
        });
        // Update User Email Text View
        meViewModel.getUserEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                userEmailText.setText(s);
            }
        });

    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e(TAG, "User Successfully Logged Out");
                        Toast.makeText(getContext(), "Successfully logged out", Toast.LENGTH_LONG);
                        Activity thisActivity = getActivity();
                        Intent intent = thisActivity.getIntent();
                        thisActivity.finish();
                        startActivity(intent);
                    }

                });
    }

    public void resetCurrentUser() {
        String authID = DataUtils.getCurrentUserAuthID();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(UserInfo.ALL_USER_PARENT).child(authID);
        DatabaseReference tmpRef = dbRef.child(CategoryInfo.ALL_CATEGORY_PARENT);
        tmpRef.removeValue();
        tmpRef = dbRef.child(UserActivityInfo.ALL_USER_ACTIVITY_PARENT);
        tmpRef.removeValue();
        tmpRef = dbRef.child("activityNames");
        tmpRef.removeValue();
        tmpRef = dbRef.child(EventInfo.ALL_EVENT_PARENT);
        tmpRef.removeValue();
    }

}