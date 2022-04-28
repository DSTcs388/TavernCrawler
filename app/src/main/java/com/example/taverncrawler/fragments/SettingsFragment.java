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
    private Switch switchVisualStyle;
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
        switchVisualStyle = view.findViewById(R.id.switchVisualStyle);
        buttonChangeUser = view.findViewById(R.id.buttonChangeUsername);
        buttonchangePass = view.findViewById(R.id.buttonChangePassword);
        txtvwChange = view.findViewById(R.id.txtvwchange);
        buttonChangeZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Changing zipcode is under construction!", Toast.LENGTH_SHORT).show();
            }
        });
        buttonChangeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String change = txtvwChange.getText().toString();
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", ParseUser.getCurrentUser());
                query.setLimit(1);
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if(e != null) {
                            Log.e("Settings", "ParseError: ", e);
                            Toast.makeText(getContext(), "A problem occurred saving your image", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ParseUser user = objects.get(0);
                        user.put("username",change);
                        user.saveInBackground();
                    }
                });

            }
        });
        buttonchangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String change = txtvwChange.getText().toString();
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", ParseUser.getCurrentUser());
                query.setLimit(1);
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if(e != null) {
                            Log.e("Settings", "ParseError: ", e);
                            Toast.makeText(getContext(), "A problem occurred saving your image", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ParseUser user = objects.get(0);
                        user.put("password",change);
                        user.saveInBackground();
                    }
                });


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