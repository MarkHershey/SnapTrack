package com.example.snaptrackapp.ui.me;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    MutableLiveData<String> userName;
    FirebaseUser userLoggedIn;

    public MeViewModel() {
        mText = new MutableLiveData<>();
        userName = new MutableLiveData<>();

        mText.setValue("This is Me fragment"); // to be removed maybe

        // get current user
        userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();
        if (userLoggedIn != null) {
            String name = userLoggedIn.getDisplayName();
            String email = userLoggedIn.getEmail();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String firebaseUserID = userLoggedIn.getUid();

            if (!name.trim().isEmpty()) {
                userName.setValue(name);
            } else {
                userName.setValue("User Name Not Set");
            }

        }else {
            userName.setValue("Not Signed In");
        }

    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getUserName() {
        return userName;
    }
}