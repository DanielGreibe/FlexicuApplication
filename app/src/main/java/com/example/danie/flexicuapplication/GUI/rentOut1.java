package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danie.flexicuapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ramotion.fluidslider.FluidSlider;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class rentOut1 extends AppCompatActivity implements OnMapReadyCallback {

    Address address = null;
    Circle circle = null;
    TextView lejeSlutTextView = null;
    TextView lejeStartTextView = null;
    ImageView lejeStartImageView = null;
    ImageView lejeSlutImageView = null;
    private int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_out1);
        FluidSlider slider = findViewById(R.id.slider);
        lejeStartTextView = findViewById(R.id.lejeStartTextView);
        lejeSlutTextView = findViewById(R.id.lejeSlutTextView);
        lejeStartImageView = findViewById(R.id.lejeStartImageView);
        lejeSlutImageView = findViewById(R.id.lejeSlutImageView);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Geocoder geocoder = new Geocoder(this);
        final String zip = "90210";
        try {
            List<Address> addresses = geocoder.getFromLocationName("2850, Denmark", 1);
            if (addresses != null && !addresses.isEmpty()) {
                address = addresses.get(0);
                // Use the address as needed
                @SuppressLint("DefaultLocale") String message = String.format("Latitude: %f, Longitude: %f",
                        address.getLatitude(), address.getLongitude());
                System.out.println("Latitude: " + address.getLatitude() + " Longi: " + address.getLongitude());
                System.out.println("List: " + addresses.toString());
            } else {
                // Display appropriate message when Geocoder services are not available
            }
        } catch (IOException e) {
            // handle exception
        }

        //Set slider settings
        int min = 5;
        int max = 150;
        slider.setPosition(1);
        slider.setColorBar(Color.rgb(0, 153, 203));
        slider.setColorBubble(Color.WHITE);
        slider.setStartText(Integer.toString(min) + " km");
        slider.setEndText(Integer.toString(max) + " km");
        slider.setBubbleText(Integer.toString((int) (((max-min)*slider.getPosition())+min)));
        slider.setPositionListener(new Function1<Float, Unit>() {
            @Override
            public Unit invoke(Float aFloat) {
                int value = (int) (((max-min)*aFloat)+min);
                circle.setRadius(value*1000);
                slider.setBubbleText(Integer.toString(value));
                return null;
            }
        });


        //Set calendar onClick listeners
        lejeStartImageView.setOnClickListener((view) ->{
            Date date = new Date();
            year = date.getYear() + 1900;
            month = date.getMonth();
            day = date.getDate();
            System.out.println("year: " + year + " Month: " + month + "day: " + day);
            showDialog(999);
        });

        //Set calendar onClick listeners
        lejeSlutImageView.setOnClickListener((view) ->{
            Date date = new Date();
            year = date.getYear() + 1900;
            month = date.getMonth();
            day = date.getDate();
            showDialog(998);
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng position = new LatLng(address.getLatitude(), address.getLongitude());
        MarkerOptions marker = new MarkerOptions()
                .position(position)
                .title("Marker");
        map.addMarker(marker);
        // Add a circle in Sydney
        circle = map.addCircle(new CircleOptions()
                .center(position)
                .radius((double)10000)
                .strokeColor(Color.rgb(0, 153, 203))
                .strokeWidth(4)
                .fillColor(Color.argb(100, 123,123,123)));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 9));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog datepicker =new DatePickerDialog(this, myDateListener, year, month, day);
            return datepicker;
        }
        if (id == 998)
            return new DatePickerDialog(this, myDateListener2, year, month, day);
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            lejeStartTextView.setText(Integer.toString(arg3)+"/"+Integer.toString(arg2+1)+"/"+Integer.toString(arg1));
            //If rental dates selected and employee is selected
            /*if(employeeSelected != 0 && textViewLejeperiodeSlut.getText().toString().contains("/") && textViewLejeperiodeStart.getText().toString().contains("/")) {
                udlejBtn.setBackgroundResource(R.drawable.layout_background_round_corners_blue);
            }*/
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            lejeSlutTextView.setText(Integer.toString(arg3)+"/"+Integer.toString(arg2+1)+"/"+Integer.toString(arg1));
            //If rental dates selected and employee is selected
            /*if(employeeSelected != 0 && textViewLejeperiodeSlut.getText().toString().contains("/") && textViewLejeperiodeStart.getText().toString().contains("/")) {
                udlejBtn.setBackgroundResource(R.drawable.layout_background_round_corners_blue);
            }*/
        }
    };
}
