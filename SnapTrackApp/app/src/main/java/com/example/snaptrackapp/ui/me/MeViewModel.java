package com.example.snaptrackapp.ui.me;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MeViewModel extends ViewModel {

    FirebaseUser userLoggedIn;

    MutableLiveData<String> userName = new MutableLiveData<>();
    MutableLiveData<String> userEmail = new MutableLiveData<>();

    public MeViewModel() {
        // get current user
        userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();
        if (userLoggedIn != null) {
            // get user name
            String name = userLoggedIn.getDisplayName();
            if (name != null && !name.trim().isEmpty()) userName.setValue(name);
            else userName.setValue("User Name Not Set");

            // get user email
            String email = userLoggedIn.getEmail();
            userEmail.setValue(email);

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String firebaseUserID = userLoggedIn.getUid();

        }else {
            userName.setValue("Not Signed In");
        }

    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<String> getUserEmail() {
        return userEmail;
    }
}