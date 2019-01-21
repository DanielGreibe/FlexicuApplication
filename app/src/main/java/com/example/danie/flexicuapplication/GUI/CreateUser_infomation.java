package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.danie.flexicuapplication.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class CreateUser_infomation extends AppCompatActivity {

    int CVR = 0;
    String CVRDATA = null;

    EditText Firmanavn, Email, Telefon, Postnummer;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_infomation);

        Firmanavn = findViewById(R.id.PasswordEditText);
        Email = findViewById(R.id.EmailEditText);
        Telefon = findViewById(R.id.TelefonEditText);
        Postnummer = findViewById(R.id.PostnummerEditText);
        nextButton = findViewById(R.id.buttonNextPage);

        String[] descriptionData = {"CVR", "Info", "Billede", "Password"};
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);

        //Get intent and parse values
        Intent intent = getIntent();
        CVR = intent.getIntExtra("CVR", 0);
        CVRDATA = intent.getStringExtra("DATA");
        System.out.println("CVRDATA" + CVRDATA);
        System.out.println("CVR " + CVR);

        //Parse JSON
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(CVRDATA);
        JsonObject CVRData = element.getAsJsonObject();

        Firmanavn.setText(CVRData.get("name").toString().replaceAll("\"", ""));
        Email.setText(CVRData.get("email").toString().replaceAll("\"", ""));
        Telefon.setText(CVRData.get("phone").toString().replaceAll("\"", ""));
        Postnummer.setText(CVRData.get("zipcode").toString().replaceAll("\"", ""));

        //Tag billede button
        nextButton.setOnClickListener((view) ->{
            Intent user_photo = new Intent(this, CreateUser_photo.class);
            user_photo.putExtra("CVR", Integer.toString(CVR));
            user_photo.putExtra("FIRMANAVN", Firmanavn.getText().toString());
            user_photo.putExtra("EMAIL", Email.getText().toString());
            user_photo.putExtra("TELEFON", Telefon.getText().toString());
            user_photo.putExtra("POSTNUMMER", Postnummer.getText().toString());
            startActivity(user_photo);
        });
    }
}
