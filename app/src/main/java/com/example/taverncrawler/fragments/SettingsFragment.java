package com.example.taverncrawler.fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class SettingsFragment extends Fragment {
    private Button buttonChangeZip;
    private Button buttonChangeUser;
    private Button buttonchangePass;
    private MaterialAutoCompleteTextView txtvwChange;
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
        buttonChangeUser = view.findViewById(R.id.buttonChangeUsername);
        buttonChangeZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeZipcodeFragment fragment = new ChangeZipcodeFragment();
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

            }
        });
        buttonChangeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeUsername fragement = new ChangeUsername();
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragement).commit();

            }
        });
    }
}