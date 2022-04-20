package com.example.taverncrawler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taverncrawler.R;

public class SettingsFragment extends Fragment {
    private Button buttonChangeZip;
    private Switch switchVisualStyle;

    public SettingsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonChangeZip = view.findViewById(R.id.buttonChangeZip);
        switchVisualStyle = view.findViewById(R.id.switchVisualStyle);

        buttonChangeZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Changing zipcode is under construction!", Toast.LENGTH_SHORT).show();
            }
        });

        switchVisualStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Changing visuals is under construction!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
