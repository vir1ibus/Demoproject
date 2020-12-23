package com.example.carsharing.ui.maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MapsViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}