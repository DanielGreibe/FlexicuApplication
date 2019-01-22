package com.example.danie.flexicuapplication;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danie.flexicuapplication.LogicLayer.AndCriteria;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaDistance;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaInterface;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaPayLower;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaPayUpper;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import static com.example.danie.flexicuapplication.LogicLayer.GlobalVariables.getFirebaseUser;

public class profileSettings extends AppCompatActivity {

    EditText Firmanavn, Email, Telefon, Postnummer;
    Button bekræftButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        Firmanavn = findViewById(R.id.PasswordEditText);
        Email = findViewById(R.id.EmailEditText);
        Telefon = findViewById(R.id.TelefonEditText);
        Postnummer = findViewById(R.id.PostnummerEditText);
        bekræftButton = findViewById(R.id.buttonNextPage);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(("Users/"+ getFirebaseUser().getUid()+"/info"));

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firmanavn.setText(String.valueOf(snapshot.child("Firmanavn").getValue()));
                Email.setText(String.valueOf(snapshot.child("Email").getValue()));
                Telefon.setText(String.valueOf(snapshot.child("Telefon").getValue()));
                Postnummer.setText(String.valueOf(snapshot.child("Postnummer").getValue()));


                if(Firmanavn.getText().toString().equals("null")) Firmanavn.setText("");
                if(Email.getText().toString().equals("null")) Email.setText("");
                if(Telefon.getText().toString().equals("null")) Telefon.setText("");
                if(Postnummer.getText().toString().equals("null")) Postnummer.setText("");
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bekræftButton.setOnClickListener(v -> {
            myRef.child("Firmanavn").setValue(Firmanavn.getText().toString());
            myRef.child("Email").setValue(Email.getText().toString());
            myRef.child("Telefon").setValue(Telefon.getText().toString());
            myRef.child("Postnummer").setValue(Postnummer.getText().toString());
            finish();
        });

    }
}
