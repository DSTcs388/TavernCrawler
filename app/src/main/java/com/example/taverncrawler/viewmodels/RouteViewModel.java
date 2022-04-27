package com.example.taverncrawler.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;

/**
 * Dynamically passing a list of data between Fragments is very complicated from what I've gathered.
 * Current solution is to create a dataset {@link RouteViewModel#selectedBars} that the RouteFragment can observe for changes, and then updating the route based on those changes
 * If there is a more convenient way to do this, it would very much be appreciated.
 */
public class RouteViewModel extends ViewModel {
    private volatile MutableLiveData<HashMap<String, List<Double>>> selectedBars = new MutableLiveData<>();

    public LiveData<HashMap<String, List<Double>>> getSelectedBars() {
        return selectedBars;
    }

    public void setSelectedBars(HashMap<String, List<Double>> bars) {
        selectedBars.setValue(bars);
    }

    public void addBarToSelection(String name, List<Double> coordinates) {
        HashMap<String, List<Double>> selected = selectedBars.getValue();
        selected.put(name, coordinates);
        selectedBars.setValue(selected);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public RouteViewModel() {
        super();
    }
}
