package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.fabric.sdk.android.Fabric;


public class Login extends AppCompatActivity implements View.OnClickListener
{
TextView textViewLoginLater;
EditText editTextUsername;
EditText editTextPassword;
Button buttonLogin;
ConstraintLayout LoginLayout;

//Network variables
private RequestQueue myRequestQueue;
private StringRequest myStringRequest;

private FirebaseAuth mAuth;
@SuppressLint("StaticFieldLeak")
@Override
protected void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    textViewLoginLater = findViewById(R.id.textViewLoginLater);
    editTextUsername = findViewById(R.id.editTextUsername);
    editTextPassword = findViewById(R.id.editTextPassword);
    buttonLogin = findViewById(R.id.buttonLogin);
    LoginLayout = findViewById(R.id.LoginLayout);


    //Init CrashLytics
    Fabric.with(this, new Crashlytics());

    //Crashlytics.getInstance().crash(); // Force a crash
    // Initialize Firebase Auth
    mAuth = FirebaseAuth.getInstance();

    buttonLogin.setOnClickListener(this);
    textViewLoginLater.setOnClickListener(this);

    sendGetRequestForCVRData("http://cvrapi.dk/api?search=%22amsiq%22&country=dk&format=xml");

}

    @Override
    public void onClick(View v) {
        if(v == textViewLoginLater)
            {
            /*
            Intent Navigation = new Intent(this, Navigation.class);
            startActivity(Navigation);
            */

            Intent PreIndlej = new Intent(this, PreIndlej.class);
            startActivity(PreIndlej);
            }
        else if ( v == buttonLogin)
            {
            if (editTextUsername.getText().toString().equals("") || editTextPassword.getText().toString().equals(""))
                {

                editTextUsername.setText("danielgreibe@gmail.com");
                editTextPassword.setText("flexicu25");
                }

                mAuth.signInWithEmailAndPassword(editTextUsername.getText().toString(), editTextPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            loginSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            textViewLoginLater.setText("Login unsuccessful!");
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

    private void sendGetRequestForCVRData(String url) {
        myRequestQueue = Volley.newRequestQueue(this);
        //Method, URL, successListener, errorListener
        //If call is not successful select from offline word-list
        myStringRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    System.out.println("RESPONSE IS: " + response);
                    System.out.println("ok");
                },
                error -> {

                }
        ); //End of 'new StringRequest' arguments

        myRequestQueue.add(myStringRequest);
    }


}

