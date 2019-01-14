package com.example.danie.flexicuapplication.GUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PreIndlej extends AppCompatActivity implements View.OnClickListener
    {
    TextView textViewDescription;
    EditText editTextZipcode;
    ImageButton buttonGetLocation;
    Button buttonNextPage;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_indlej);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //editTextZipcode = findViewById(R.id.editTextZipcode);

        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);
        textViewDescription = findViewById(R.id.textViewDescription);
        buttonGetLocation = findViewById(R.id.buttonGetLocation);
        buttonGetLocation.setOnClickListener(this);
        buttonGetLocation.setImageResource(R.drawable.marker);
        buttonGetLocation.setBackgroundDrawable(null);


        }

    @Override
    public void onClick(View v)
        {
        if (v == buttonGetLocation)
            {


            //If you do not have permission to get GPS Data.
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                }
            else
                {
                Log.e("Permission", "You already had permission");
                }

            mFusedLocationClient.getLastLocation()
                                .addOnSuccessListener(this, new OnSuccessListener<Location>()
                                    {
                                    @Override
                                    public void onSuccess(Location location)
                                        {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null)
                                            {
                                            /*
                                            Log.e("Test", location.getLongitude() + "");
                                            Log.e("Test", location.getLatitude() + "");
                                            */
                                            Geocoder geocoder = new Geocoder(PreIndlej.this, Locale.getDefault());
                                            try
                                                {
                                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                                ((GlobalVariables) PreIndlej.this.getApplication()).setUserAddress(addresses.get(0));
                                                List<Address> addresses2 = geocoder.getFromLocationName("Kongens Lyngby" , 1);
                                                Log.e("Address2" , "Latitude: " + addresses2.get(0).getLatitude());
                                                Log.e("Address2" , "Longitude " + addresses2.get(0).getLongitude());
                                                Log.e("Address1", addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName());
                                                editTextZipcode.setText(addresses.get(0).getPostalCode() + "  -  " + addresses.get(0).getLocality());

                                                Log.e("Addresse" , ((GlobalVariables) PreIndlej.this.getApplication()).getUserAddress().getPostalCode());
                                                } catch (IOException e)
                                                {
                                                e.printStackTrace();
                                                }
                                            }
                                        }
                                    });


            }
        else if ( v == buttonNextPage)
            {
            if ( editTextZipcode.getText().length() < 4)
                {
                Toast.makeText(this, "Tryk på knappen eller indtast et postnummer", Toast.LENGTH_SHORT).show();
                }
            else
                {
                Intent RentIn = new Intent(this, RentIn.class);
                startActivity(RentIn);
                }
            }

        }

    }
