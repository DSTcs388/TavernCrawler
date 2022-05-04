package com.example.taverncrawler.fragments;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.taverncrawler.R;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {
    Button changebtn;
    EditText txtev;
    public static final String TAG = "ChangePasswordFragment";

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    public interface ChangePasswordCallback {
        void onPasswordSaved();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changebtn = (Button) view.findViewById(R.id.buttonConfirmPasswordChange);
        txtev = (EditText) view.findViewById(R.id.edTextPassword);

        if(ParseUser.getCurrentUser() == null || !ParseUser.getCurrentUser().isAuthenticated()) {
            Log.i(TAG, "There's a problem here");
            return;
        }
        changebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog loading = new ProgressDialog(requireActivity());
                loading.setTitle("Changing Password...");
                loading.show();
                loading.setCanceledOnTouchOutside(false);
                String change = txtev.getText().toString();
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
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
                        finalizeChangePassword(user, change, loading, new ChangePasswordCallback() {
                            @Override
                            public void onPasswordSaved() {
                                ParseUser.logInInBackground(user.getUsername(), user.getString("password"), new LogInCallback() {
                                    @Override
                                    public void done(ParseUser user, ParseException e) {
                                        txtev.setText("");
                                        Toast.makeText(requireActivity(), "Password successfully saved!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void finalizeChangePassword(ParseUser user, String change, ProgressDialog loading, ChangePasswordCallback changePasswordCallback) {
        user.put("username", change);
        loading.dismiss();
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                changePasswordCallback.onPasswordSaved();
            }
        });
    }
}