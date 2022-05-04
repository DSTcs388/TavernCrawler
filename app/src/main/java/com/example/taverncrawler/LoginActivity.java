package com.example.taverncrawler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

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
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
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
                    loading.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    CharSequence[] options = {"Okay"};
                    builder.setTitle("Invalid Username/Password");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.e(TAG, "Something went wrong with Parse", e);
                            etUsername.setText("");
                            etPassword.setText("");
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
                else {
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                loading.dismiss();
                login(username);
                }
            }
        });
    }

}