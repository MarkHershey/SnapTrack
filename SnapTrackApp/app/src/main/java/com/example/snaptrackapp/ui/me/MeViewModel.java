package com.example.snaptrackapp.ui.me;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.snaptrackapp.data.DataUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MeViewModel extends ViewModel {

    FirebaseUser userLoggedIn;

    MutableLiveData<String> userNameText = new MutableLiveData<>();
    MutableLiveData<String> userEmailText = new MutableLiveData<>();

    public MeViewModel() {
        // get current user
        userLoggedIn = DataUtils.getCurrentUser();
        if (userLoggedIn != null) {
            // get user name
            String name = userLoggedIn.getDisplayName();
            if (name != null && !name.trim().isEmpty()) userNameText.setValue(name);
            else userNameText.setValue("User Name Not Set");

            // get user email
            String email = userLoggedIn.getEmail();
            userEmailText.setValue(email);

        }else {
            userNameText.setValue("Not Signed In");
        }
    }

    public LiveData<String> getUserName() {
        return userNameText;
    }

    public LiveData<String> getUserEmail() {
        return userEmailText;
    }
}
