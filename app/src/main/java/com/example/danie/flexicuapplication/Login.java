package com.example.danie.flexicuapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.security.AccessController.getContext;

public class Login extends AppCompatActivity implements View.OnClickListener
{
TextView LoginLater;
EditText UsernameView;
EditText PasswordView;
Button Login;
ConstraintLayout LoginLayout;
private FirebaseAuth mAuth;
@Override
protected void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    LoginLater = findViewById(R.id.LoginLaterView);
    UsernameView = findViewById(R.id.UsernameView);
    PasswordView = findViewById(R.id.PasswordView);
    Login = findViewById(R.id.LoginButton);
    LoginLayout = findViewById(R.id.LoginLayout);

    // Initialize Firebase Auth
    mAuth = FirebaseAuth.getInstance();

    Login.setOnClickListener(this);
    LoginLater.setOnClickListener(this);

/*
    UsernameView.setText("Runtime Changed Text");
    PasswordView.setText("Runtime Changed Text");
    Login.setText("Runtime Changed Text");
    LoginLater.setText("Changed Text on Login Later button");
    */
}

   /* @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent Navigation = new Intent(this, Navigation.class);
        startActivity(Navigation);
    }*/


    @Override
    public void onClick(View v) {
        if(v == LoginLater)
            {
            Intent Navigation = new Intent(this, Navigation.class);
            startActivity(Navigation);
            }
        else if ( v == Login)
            {
                mAuth.signInWithEmailAndPassword(UsernameView.getText().toString(), PasswordView.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            loginSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            LoginLater.setText("Login unsuccessful!");
                        }
                    }
                });
            }
    }

    public void loginSuccess(FirebaseUser user){
        ((GlobalVariables) this.getApplication()).setFirebaseUser(user);
        Intent Navigation = new Intent(this, Navigation.class);
        startActivity(Navigation);
    }


}

