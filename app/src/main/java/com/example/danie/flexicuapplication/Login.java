package com.example.danie.flexicuapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static java.security.AccessController.getContext;

public class Login extends AppCompatActivity implements View.OnClickListener
    {
    TextView LoginLater;
    EditText UsernameView;
    EditText PasswordView;
    Button Login;
    ConstraintLayout LoginLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("");
        if( getActionBar() != null)
            {
            getActionBar().setIcon(R.drawable.flexicu_toolbar);
            }
        LoginLater = findViewById(R.id.LoginLaterView);
        UsernameView = findViewById(R.id.UsernameView);
        PasswordView = findViewById(R.id.PasswordView);
        Login = findViewById(R.id.LoginButton);
        LoginLayout = findViewById(R.id.LoginLayout);



        LoginLater.setOnClickListener(this);

/*
        UsernameView.setText("Runtime Changed Text");
        PasswordView.setText("Runtime Changed Text");
        Login.setText("Runtime Changed Text");
        LoginLater.setText("Changed Text on Login Later button");
        */



        }

    @Override
    public void onClick(View v)
        {
        if(v == LoginLater)
            {
            Intent Navigation = new Intent(this, Navigation.class);
            startActivity(Navigation);
            }
        }
    }
