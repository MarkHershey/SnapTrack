package com.example.snaptrackapp.ui.me;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    MutableLiveData<String> userName;

    public MeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Me fragment");
        userName = new MutableLiveData<>();
        userName.setValue("Mark Huang");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getUserName() {
        return userName;
    }
}