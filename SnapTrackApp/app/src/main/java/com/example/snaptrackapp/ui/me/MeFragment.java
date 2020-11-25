package com.example.snaptrackapp.ui.me;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snaptrackapp.MainActivity;
import com.example.snaptrackapp.R;

import org.w3c.dom.Text;

public class MeFragment extends Fragment {

    private MeViewModel meViewModel;

    TextView userNameText;
    Button signOutButton;

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_me, container, false);
        userNameText = root.findViewById(R.id.userNameTextView);
        signOutButton = root.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Logging out", Toast.LENGTH_LONG).show();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        meViewModel = new ViewModelProvider(this).get(MeViewModel.class);


        // Update User name
        meViewModel.getUserName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                userNameText.setText(s);
            }
        });


    }

}