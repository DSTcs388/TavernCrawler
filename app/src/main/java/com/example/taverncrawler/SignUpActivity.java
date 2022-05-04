package com.example.taverncrawler;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.taverncrawler.models.User;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "SignUpActivity";
    public EditText etSignUpUsername, etSignUpPassword, etConfirmPassword, etEmail, etZipcode;
    public Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        etSignUpUsername = (EditText) findViewById(R.id.etSignUpUsername);
        etSignUpPassword = (EditText) findViewById(R.id.etSignUpPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etZipcode = (EditText) findViewById(R.id.etZipcode);
        signUp = (Button) findViewById(R.id.btnSignUpButton);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etSignUpPassword.getText().toString().equals(etSignUpPassword.getText().toString()))
                {
                    CharSequence[] options = {"Okay"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setTitle("Passwords do not match!");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(options[i].equals("Okay")) {
                                dialogInterface.dismiss();
                            }
                        }
                    });
                    return;
                }
                String username = etSignUpUsername.getText().toString();
                String password = etSignUpPassword.getText().toString();
                String email = etEmail.getText().toString();
                String zipcode = etZipcode.getText().toString();

                boolean correctFormat = zipcode.matches("[0-9]{5}") | zipcode.matches("[0-9]{5}-[0-9]{4}");
                if (!correctFormat) {
                    CharSequence[] options = {"Okay"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setTitle("Invalid Zipcode");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(options[i].equals("Okay")) {
                                dialogInterface.dismiss();
                            }
                        }
                    });
                    return;
                }

                User newPerson = new User();
                newPerson.setUsername(username);
                newPerson.setPassword(password);
                newPerson.setZipcode(zipcode);
                newPerson.setEmail(email);
                newPerson.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Log.i("Parse Result","Successful!");
                            Log.i("Current User",ParseUser.getCurrentUser().getUsername());
                            loginUser(username,password);
                        }
                        else{
                            Log.i("Parse Result","Failed " + e.toString());
                        }
                    }
                });

            }
        });
    }
    private void login(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

    private void loginUser(String username, String password) {
        ProgressDialog loading = new ProgressDialog(this);
        loading.setTitle("Logging in...");
        loading.show();
        loading.setCanceledOnTouchOutside(false);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Something went wrong with Parse", e);
                    return;
                }
                Toast.makeText(SignUpActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                loading.dismiss();
                login(username);
            }
        });
    }
}