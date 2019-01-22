package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.LogicLayer.RoundedImageView;
import com.example.danie.flexicuapplication.R;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;


public class Login extends AppCompatActivity implements View.OnClickListener
{
TextView textViewLoginLater, saveloginTV;
EditText editTextUsername;
EditText editTextPassword;
Button buttonLogin;
Button opretBruger;
ConstraintLayout LoginLayout;
SharedPreferences settings;
SharedPreferences.Editor editor;
LabeledSwitch saveLog;
ProgressBar proBar;

boolean saveData = true;

private FirebaseAuth mAuth;
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
@SuppressLint("StaticFieldLeak")
@Override
protected void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    opretBruger = findViewById(R.id.buttonCreateUser);
    textViewLoginLater = findViewById(R.id.textViewLoginLater);
    editTextUsername = findViewById(R.id.editTextUsername);
    editTextPassword = findViewById(R.id.editTextPassword);
    buttonLogin = findViewById(R.id.buttonLogin);
    LoginLayout = findViewById(R.id.LoginLayout);
    saveLog = findViewById(R.id.saveLogin);
    saveloginTV = findViewById(R.id.saveloginTV);
    saveLog.setOnClickListener(this);
    saveloginTV.setOnClickListener(this);
    buttonLogin.setOnClickListener(this);
    textViewLoginLater.setOnClickListener(this);
    opretBruger.setOnClickListener(this);
    proBar = findViewById(R.id.progressBar);
    proBar.setVisibility(View.INVISIBLE);

    settings = getSharedPreferences("prefs",0);
    editor = settings.edit();
    buttonLogin.setElevation(8);
    opretBruger.setElevation(8);


    //Toggle button
    saveLog.setColorBorder(R.color.FlexGreen);
    saveLog.setLabelOn("Til");
    saveLog.setLabelOff("Fra");

    String loginInfo = settings.getString("login","");
    if (!loginInfo.equals("")){
        String[] temp;
        temp = loginInfo.split(",");
        editTextUsername.setText(temp[0]);
        editTextPassword.setText(temp[1]);
       }


    //Init CrashLytics
    Fabric.with(this, new Crashlytics());

    //Crashlytics.getInstance().crash(); // Force a crash to test Crashlytics
    // Initialize Firebase Auth
    mAuth = FirebaseAuth.getInstance();

}

    @Override
    public void onClick(View v) {
        if ( v == buttonLogin) {
            proBar.setVisibility(View.VISIBLE);
            if (!editTextUsername.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")) {
                mAuth.signInWithEmailAndPassword(editTextUsername.getText().toString(), editTextPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(saveData){
                                editor.putString("login", editTextUsername.getText().toString() + "," + editTextPassword.getText().toString());
                                editor.commit();
                            }
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            loginSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            textViewLoginLater.setText("Login unsuccessful!");
                            proBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        } else if(v == saveLog){
            saveData = !saveData;
        } else if(v == opretBruger){
            Intent Navigation = new Intent(this, CreateUser_cvr.class);
            startActivity(Navigation);
        }
    }

    public void loginSuccess(FirebaseUser user){
        ((GlobalVariables) this.getApplication()).setFirebaseUser(user);
        Intent Navigation = new Intent(this, Navigation.class);
        startActivity(Navigation);
        finish();
    }


}

