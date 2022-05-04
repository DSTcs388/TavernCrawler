package com.example.taverncrawler.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

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
public class ChangeZipcodeFragment extends Fragment {

    public ChangeZipcodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_zipcode, container, false);
        Context context = view.getContext();
        Button buttonchange =(Button) view.findViewById(R.id.btnchangezip);
        EditText txtchange = view.findViewById(R.id.edtxtchangezip);
        buttonchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String change = txtchange.getText().toString();
                    char[] validString = change.toCharArray();
                    for(char c : validString) {
                        if(!Character.isDigit(c)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                            CharSequence[] options = {"Okay"};
                            builder.setTitle("Invalid Zipcode");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(options[i].equals("Okay")) {
                                        dialogInterface.dismiss();
                                    }
                                }
                            });
                            builder.show();
                            return;
                        }
                    }
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
                            user.put("zipcode",change);
                            user.saveInBackground();
                            txtchange.setText("");
                            Toast.makeText(requireActivity(), "Successfully saved zipcode!", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });

        return view;
    }
}