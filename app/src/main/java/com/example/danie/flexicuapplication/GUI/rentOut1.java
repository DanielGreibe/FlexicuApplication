package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.danie.flexicuapplication.LogicLayer.CrudEmployee;
import com.example.danie.flexicuapplication.LogicLayer.CrudRentOut;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.LogicLayer.RoundedImageView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ramotion.fluidslider.FluidSlider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class rentOut1 extends AppCompatActivity implements OnMapReadyCallback {

    Address address = null;
    Circle circle = null;
    TextView lejeSlutTextView = null;
    TextView lejeStartTextView = null;
    ImageView lejeStartImageView = null;
    ImageView lejeSlutImageView = null;
    TextView annoneceTextView = null;
    TextView beskrivelse1TextView = null;
    ImageView profilBilledeImageView = null;
    Button bekræftButton = null;
    boolean startDate = false;
    boolean endDate = false;
    //Employee variables
    String pictureURl = null;
    int afstand;
    int startYear, startMonth, startDay;
    int endYear, endMonth, endDay;
    String ID;
    String navn;
    String job;
    String rank;
    String pay;
    String postnummer;
    String owner;

    @SuppressLint({"SetTextI18n", "StaticFieldLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_out1);
        FluidSlider slider = findViewById(R.id.slider);
        lejeStartTextView = findViewById(R.id.lejeStartTextView);
        lejeSlutTextView = findViewById(R.id.lejeSlutTextView);
        lejeStartImageView = findViewById(R.id.lejeStartImageView);
        lejeSlutImageView = findViewById(R.id.lejeSlutImageView);
        annoneceTextView = findViewById(R.id.annonceTextView);
        beskrivelse1TextView = findViewById(R.id.beskrivelseEtTextView);
        profilBilledeImageView = findViewById(R.id.profileImageView);
        bekræftButton = findViewById(R.id.bekræft);

        //Set database refrences
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefUdlejid = database.getReference("Users/" + GlobalVariables.getFirebaseUser().getUid() + "/Udlejninger");
        DatabaseReference myRefUdlejninger = database.getReference("Udlejninger");

        //Get intent
        String entryString = getIntent().getStringExtra("entryString");


        //Hent data og gør det til et JsonObject
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(entryString);
        JsonObject Employee = element.getAsJsonObject();

        navn = Employee.get("name").toString().replaceAll("\"", "");
        postnummer = Employee.get("zipcode").toString().replaceAll("\"", "");
        afstand = Integer.parseInt(Employee.get("dist").toString().replaceAll("\"", ""));
        pictureURl = Employee.get("pic").toString().replaceAll("\"", "");
        ID = Employee.get("ID").toString().replaceAll("\"", "");
        job = Employee.get("job").toString().replaceAll("\"", "");
        rank = Employee.get("rank").toString().replaceAll("\"", "");
        pay = Employee.get("pay").toString().replaceAll("\"", "");
        owner = Employee.get("owner").toString().replaceAll("\"", "");
        bekræftButton.setEnabled(false);
        bekræftButton.setBackgroundColor(Color.GRAY);

        annoneceTextView.setText("Opret annonce for\n" + navn);
        if (navn.contains(" ")) {
            String tempNavn = navn.substring(0, navn.indexOf(" "));
            beskrivelse1TextView.setText("Annoncen for " + tempNavn + " vil blive udbydt i nedenstående område. Du kan ændre området ved at trække i slideren nedenfor");
        } else {
            beskrivelse1TextView.setText("Annoncen for " + navn + " vil blive udbydt i nedenstående område. Du kan ændre området ved at trække i slideren nedenfor");
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(postnummer + ", Denmark", 1);
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
        slider.setPosition(afstand / max);
        slider.setColorBar(Color.rgb(0, 153, 203));
        slider.setColorBubble(Color.WHITE);
        slider.setStartText(Integer.toString(min) + " km");
        slider.setEndText(Integer.toString(max) + " km");
        slider.setBubbleText(Integer.toString((int) (((max - min) * slider.getPosition()) + min)));
        slider.setPositionListener(new Function1<Float, Unit>() {
            @Override
            public Unit invoke(Float aFloat) {
                int value = (int) (((max - min) * aFloat) + min);
                circle.setRadius(value * 1000);
                slider.setBubbleText(Integer.toString(value));
                afstand = value;
                return null;
            }
        });


        //Set calendar onClick listeners
        lejeStartImageView.setOnClickListener((view) -> {
            Date date = new Date();
            startYear = date.getYear() + 1900;
            startMonth = date.getMonth();
            startDay = date.getDate();
            showDialog(999);
        });

        //Set calendar onClick listeners
        lejeSlutImageView.setOnClickListener((view) -> {
            Date date = new Date();
            endYear = date.getYear() + 1900;
            endMonth = date.getMonth();
            endDay = date.getDate();
            showDialog(998);
        });

        bekræftButton.setOnClickListener((view) -> {
            if (lejeStartTextView.getText().toString().contains("/") && lejeSlutTextView.getText().toString().contains("/")) {
                CrudRentOut newRentOut = new CrudRentOut(GlobalVariables.getFirebaseUser().getUid() + ID, navn, job, pictureURl, lejeStartTextView.getText().toString(), lejeSlutTextView.getText().toString(), rank, pay, postnummer, afstand, owner, "Sat til udleje");
                Gson gson = new Gson();
                String rentOutJSON = gson.toJson(newRentOut);
                myRefUdlejninger.child(Integer.toString(newRentOut.getRentId())).setValue(rentOutJSON);
                String rentOutIdJSON = gson.toJson("" + GlobalVariables.getFirebaseUser().getUid() + ID);
                myRefUdlejid.child(Integer.toString(newRentOut.getRentId())).setValue(rentOutIdJSON);
            }

            CrudEmployee employee = JsonToPersonConverter(entryString, "sat til udleje");
            Gson gson = new Gson();
            String jsonEmployee = gson.toJson(employee);
            DatabaseReference updateEmployee = database.getReference("Users/" + GlobalVariables.getFirebaseUser().getUid() + "/Medarbejdere/");
            //Load employees and create cardviews and add to scroller
            updateEmployee.child(employee.getID()).setValue(jsonEmployee);
            System.out.println("HERE1 " + employee.toString());
            Intent intent = new Intent(this, TabbedRentOut.class);
            intent.putExtra("callingActivity", "udlejActivity");
            startActivity(intent);
            finish();
        });


        //Load picture
        if (pictureURl.equals("flexicu")) {
            int minPixels = 0;
            Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.flexiculogocube);
            if (photo.getWidth() < photo.getHeight()) {
                minPixels = photo.getWidth();
            } else {
                minPixels = photo.getHeight();
                Bitmap squareImg = Bitmap.createBitmap(photo, ((photo.getWidth() - minPixels) / 2), ((photo.getHeight() - minPixels) / 2), minPixels, minPixels);
                squareImg = RoundedImageView.getCroppedBitmap(squareImg, 400);
                profilBilledeImageView.setImageBitmap(squareImg);
            }
        } else {
            //System.out.println(src);
            URL url = null;
            try {
                url = new URL(pictureURl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //We want to download images for the list of workers
            URL finalUrl = url;
            new AsyncTask<Void, Void, Bitmap>() {
                //Get pictures in background
                @SuppressLint("WrongThread")
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    try {
                        //Use glide for faster load and to save images in cache! (glide.asBitmap does not create its own asynctask)
                        Bitmap myBitmap = Glide
                                .with(profilBilledeImageView)
                                .asBitmap()
                                .load(finalUrl)
                                .submit()
                                .get();
                        return myBitmap;
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                //On return update images in list
                @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
                @Override
                protected void onPostExecute(Bitmap s) {
                    super.onPostExecute(s);
                    s = RoundedImageView.getCroppedBitmap(s, 200);
                    profilBilledeImageView.setImageBitmap(s);
                }
            }.execute();
        }
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
                .radius((double) afstand * 1000)
                .strokeColor(Color.rgb(0, 153, 203))
                .strokeWidth(4)
                .fillColor(Color.argb(100, 123, 123, 123)));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 9));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog datepicker = new DatePickerDialog(this, myDateListener, startYear, startMonth, startDay);
            return datepicker;
        }
        if (id == 998)
            return new DatePickerDialog(this, myDateListener2, endYear, endMonth, endDay);
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            lejeStartTextView.setText(Integer.toString(arg3) + "/" + Integer.toString(arg2 + 1) + "/" + Integer.toString(arg1));
            startDate = true;

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
            lejeSlutTextView.setText(Integer.toString(arg3) + "/" + Integer.toString(arg2 + 1) + "/" + Integer.toString(arg1));
            endDate = true;
            if (endDate && startDate) {
                validateDate();
            }
            //If rental dates selected and employee is selected
            /*if(employeeSelected != 0 && textViewLejeperiodeSlut.getText().toString().contains("/") && textViewLejeperiodeStart.getText().toString().contains("/")) {
                udlejBtn.setBackgroundResource(R.drawable.layout_background_round_corners_blue);
            }*/
        }
    };

    public void validateDate() {
        String[] startDate = lejeStartTextView.getText().toString().split("/");
        String[] endDate = lejeSlutTextView.getText().toString().split("/");
        if (Integer.parseInt(startDate[0] + startDate[1] + startDate[2]) > Integer.parseInt(endDate[0] + endDate[1] + endDate[2])) {
            lejeStartTextView.setTextColor(Color.RED);
            lejeSlutTextView.setTextColor(Color.RED);
            bekræftButton.setEnabled(true);
            bekræftButton.setBackgroundResource(R.drawable.layout_background_round_corner_blue_black_edge);
        }

    }

    public CrudEmployee JsonToPersonConverter(String entry, String status) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(entry);
        JsonObject obj = element.getAsJsonObject();

        CrudEmployee people = new CrudEmployee.EmployeBuilder(
                obj.get("name").toString().replace("\"", ""))
                .job(obj.get("job").toString().replace("\"", ""))
                .ID(obj.get("ID").toString().replaceAll("\"", ""))
                .pic(obj.get("pic").toString().replaceAll("\"", ""))
                .owner(obj.get("owner").toString().replaceAll("\"", ""))
                .pay(Double.parseDouble(obj.get("pay").toString().replaceAll("\"", "")))
                .status(status)
                .owner(obj.get("owner").toString().replaceAll("\"", ""))
                .dist(Integer.parseInt(obj.get("dist").toString().replaceAll("\"", "")))
                .zipcode(Integer.parseInt(obj.get("zipcode").toString().replaceAll("\"", "")))
                .builder();
        return people;
    }
}