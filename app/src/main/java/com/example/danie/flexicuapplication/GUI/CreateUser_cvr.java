package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.danie.flexicuapplication.R;
import com.kofigyan.stateprogressbar.StateProgressBar;

import static com.example.danie.flexicuapplication.R.drawable.layout_background_round_corners_blue;
import static com.example.danie.flexicuapplication.R.drawable.layout_background_round_corners_gray;

public class CreateUser_cvr extends AppCompatActivity {

    //Network variables
    private RequestQueue myRequestQueue;
    private StringRequest myStringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_cvr);

        //get views
        EditText cvrFelt = findViewById(R.id.PasswordEditText);
        Button næste = findViewById(R.id.buttonNextPage);
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);

        //Setup progress bar
        String[] descriptionData = {"CVR", "Info", "Billede", "Password"};
        stateProgressBar.setStateDescriptionData(descriptionData);

        //Prefill for ease of evaluation
        cvrFelt.setText("39762226");


        //Set onclick for nextbutton
        næste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get CVR-data and start next activity
                sendGetRequestForCVRData(Integer.parseInt(cvrFelt.getText().toString()));
            }
        });

        cvrFelt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Let button have correct color
                if(cvrFelt.getText().length() == 8){
                    næste.setBackgroundResource(layout_background_round_corners_blue);
                }else{
                    næste.setBackgroundResource(layout_background_round_corners_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void sendGetRequestForCVRData(int CVR) {
        //Start get-request and wait for response
        myRequestQueue = Volley.newRequestQueue(this);
        //Method, URL, successListener, errorListener
        myStringRequest = new StringRequest(
                Request.Method.GET,
                "https://cvrapi.dk/api?search="+CVR+"&country=dk",
                response -> {
                    //start intent with received data
                    Intent user_information = new Intent(this, CreateUser_infomation.class);
                    user_information.putExtra("CVR", CVR);
                    user_information.putExtra("DATA", response);
                    startActivity(user_information);
                },
                error -> {
                    //TODO Add error handler
                }
        ); //End of 'new StringRequest' arguments

        myRequestQueue.add(myStringRequest);
    }

}
