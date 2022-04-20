package com.example.taverncrawler.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

/**
 * Dynamically passing a list of data between Fragments is very complicated from what I've gathered.
 * Current solution is to create a dataset {@link RouteViewModel#selectedBars} that the RouteFragment can observe for changes, and then updating the route based on those changes
 * If there is a more convient way to do this, it would very much be appreciated.
 */
public class RouteViewModel extends ViewModel {
    private final MutableLiveData<List<String>> selectedBars = new MutableLiveData<>();

    public LiveData<List<String>> getSelectedBars() {
        return selectedBars;
    }

    public void setSelectedBars(List<String> bars) {
        selectedBars.setValue(bars);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public RouteViewModel() {
        super();
    }
}
