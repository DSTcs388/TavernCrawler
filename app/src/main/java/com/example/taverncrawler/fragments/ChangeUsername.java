package com.example.taverncrawler.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taverncrawler.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeUsername extends Fragment {

    public ChangeUsername() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       return inflater.inflate(R.layout.fragment_change_username, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button changebtn = (Button) view.findViewById(R.id.btnConfirmChangeUsername);
        EditText txtev = view.findViewById(R.id.etConfirmChangeUsername);
        changebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String change = txtev.getText().toString();
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                query.setLimit(1);
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if(e != null) {
                            Log.e("Settings", "ParseError: ", e);
                            return;
                        }
                        ParseUser user = objects.get(0);
                        user.put("username", change);
                        user.saveInBackground();
                        txtev.setText("");
                        Toast.makeText(requireActivity(), "Username successfully changed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}