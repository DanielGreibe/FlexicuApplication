package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danie.flexicuapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kofigyan.stateprogressbar.StateProgressBar;

import static com.example.danie.flexicuapplication.R.drawable.layout_background_round_corners_blue;
import static com.example.danie.flexicuapplication.R.drawable.layout_background_round_corners_gray;

public class CreateUser_password extends AppCompatActivity {

    String truepassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_password);

        //Get intent
        Intent intent = getIntent();

        //Get Views
        EditText password = findViewById(R.id.PasswordEditText);
        EditText passwordRepeat = findViewById(R.id.PasswordRepeatEditText);
        Button nextButton = findViewById(R.id.buttonNextPage);
        TextView repeatTextView = findViewById(R.id.repeatTextView);

        //Setup progressbar
        String[] descriptionData = {"CVR", "Info", "Billede", "Password"};
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);

        //Set repeat input invisible
        repeatTextView.setVisibility(View.INVISIBLE);
        passwordRepeat.setVisibility(View.INVISIBLE);

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //Check if password meets requirements
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password.getText().toString().length() >= 6) {
                    //Set repeat input visible
                    repeatTextView.setVisibility(View.VISIBLE);
                    passwordRepeat.setVisibility(View.VISIBLE);
                    password.setError(null);
                }else{
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordRepeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //Check if both passwords are the same and if they meet requirements
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(passwordRepeat.getText().toString().equals(password.getText().toString())) {
                    nextButton.setBackgroundResource(R.drawable.layout_background_round_corners_blue);
                    passwordRepeat.setError(null);
                    if(passwordRepeat.getText().toString().length() >= 6) {
                        truepassword = passwordRepeat.getText().toString();
                    }else{
                        truepassword = null;
                        nextButton.setBackgroundResource(R.drawable.layout_background_round_corners_gray);
                    }
                }else{
                    nextButton.setBackgroundResource(R.drawable.layout_background_round_corners_gray);
                    truepassword = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Next button check if password requirements are OK
        nextButton.setOnClickListener((view) ->{
            //check if password requirements are OK
            if(truepassword == null){
                if(password.getText().length() < 6) password.setError("Mindst 6 tegn!");
                if(!passwordRepeat.getText().toString().equals(password.getText().toString())) passwordRepeat.setError("Passwords er ikke ens!");
                return;
            }

            //Add user to firebase
            FirebaseAuth firebase = FirebaseAuth.getInstance();
            firebase.createUserWithEmailAndPassword(intent.getStringExtra("EMAIL"), truepassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //TODO tilføj data til firabaseDatabase / user
                    }else{
                        //TODO tilføj error message'
                    }

                }
            });

            //Go back to login
            Intent login = new Intent(this ,Login.class);
            login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            finish();
        });
    }
}
