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
import com.google.firebase.database.FirebaseDatabase;

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

        EditText password = findViewById(R.id.PasswordEditText);
        EditText passwordRepeat = findViewById(R.id.PasswordRepeatEditText);
        Button nextButton = findViewById(R.id.buttonNextPage);
        TextView repeatTextView = findViewById(R.id.repeatTextView);
        TextView warning1 = findViewById(R.id.warningTextView1);
        TextView warning2 = findViewById(R.id.warningTextView2);

        repeatTextView.setVisibility(View.INVISIBLE);
        passwordRepeat.setVisibility(View.INVISIBLE);
        warning1.setVisibility(View.INVISIBLE);
        warning2.setVisibility(View.INVISIBLE);

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password.getText().toString().length() >= 6) {
                    repeatTextView.setVisibility(View.VISIBLE);
                    passwordRepeat.setVisibility(View.VISIBLE);
                    warning1.setVisibility(View.INVISIBLE);
                }else{
                    warning1.setVisibility(View.VISIBLE);
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

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(passwordRepeat.getText().toString().equals(password.getText().toString())) {
                    nextButton.setBackgroundResource(R.drawable.layout_background_round_corners_blue);
                    warning2.setVisibility(View.INVISIBLE);
                    if(passwordRepeat.getText().toString().length() >= 6) {
                        truepassword = passwordRepeat.getText().toString();
                    }else{
                        truepassword = null;
                        nextButton.setBackgroundResource(R.drawable.layout_background_round_corners_gray);
                    }
                }else{
                    warning2.setVisibility(View.VISIBLE);
                    nextButton.setBackgroundResource(R.drawable.layout_background_round_corners_gray);
                    truepassword = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Next button
        nextButton.setOnClickListener((view) ->{
            if(truepassword == null) return;

            FirebaseAuth firebase = FirebaseAuth.getInstance();
            firebase.createUserWithEmailAndPassword(intent.getStringExtra("EMAIL"), truepassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //TODO tilføj data til firabaseDatabase / user
                    }else{
                        //TODO tilføj error message'
                        warning1.setText("Fejl ved oprettelse i database!");
                    }

                }
            });
            Intent login = new Intent(this ,Login.class);
            startActivity(login);
        });

    }
}
