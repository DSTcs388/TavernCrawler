package com.example.taverncrawler.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taverncrawler.R;
import com.example.taverncrawler.RestClient;
import com.example.taverncrawler.viewmodels.RouteViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteFragment extends Fragment {
    private double longitude, latitude;
    private final RestClient client = new RestClient();
    private List<String> selectedBars = new ArrayList<>();
    private RouteViewModel routeViewModel;

    public RouteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        routeViewModel = new ViewModelProvider(requireActivity()).get(RouteViewModel.class);
        routeViewModel.getSelectedBars().observe(getViewLifecycleOwner(), strings -> {
            //Update UI With additional bars
        });
    }
}