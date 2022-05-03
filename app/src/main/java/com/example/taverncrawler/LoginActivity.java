package com.example.taverncrawler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.os.Parcelable.ClassLoaderCreator;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        if(ParseUser.getCurrentUser() != null) {
            login(ParseUser.getCurrentUser().getUsername());
        }

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        Button SignUp = (Button)findViewById(R.id.btnSignup);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Button clicked");
                String username = etUsername.getText().toString();
                String password = etUsername.getText().toString();
                loginUser(username, password);
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etUsername.getText().toString();
                ParseUser newperson = new ParseUser();
                newperson.setUsername(username);
                newperson.setPassword(password);
                newperson.signUpInBackground(new SignUpCallback() {
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
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                loading.dismiss();
                login(username);
            }
        });
    }

}