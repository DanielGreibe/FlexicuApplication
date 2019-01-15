package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.danie.flexicuapplication.LogicLayer.CriteriaDemo;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class RentOut extends AppCompatActivity implements View.OnClickListener {
    //Date picker variables
    private Calendar calendar;
    private int year, month, day;

    //Visual logic
    boolean employeeSelected = false;

    //Storage ref
    private StorageReference mStorageRef;


    private Context mContext;
    private ConstraintLayout constLayout;
    private ConstraintLayout lejeStart, lejeSlut;
    private ConstraintLayout constCardLayout;
    TextView textViewErhverv, textViewLejeperiodeStart, textViewRadius, textViewPostnummer, textViewLøn, textViewLejeperiodeSlut;
    private ConstraintLayout udlejBtn;
    int id = 1;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_out);

        lejeSlut = findViewById(R.id.lejeSlut);
        lejeStart = findViewById(R.id.lejeStart);

        textViewRadius = findViewById(R.id.radiusTextView);
        textViewPostnummer = findViewById(R.id.postnummerTextView);
        textViewLøn = findViewById(R.id.lønTextView);
        textViewLejeperiodeSlut = findViewById(R.id.lejeSlutTextView);
        textViewErhverv = findViewById(R.id.erhvervTextView);
        textViewLejeperiodeStart = findViewById(R.id.lejeStartTextView);
        mContext = getApplicationContext();
        LinearLayout myContainer = findViewById(R.id.scrollLayoutUdlej);
        constLayout = findViewById(R.id.opretMedarbejder);
        udlejBtn = findViewById(R.id.UdlejBtn);
        CriteriaDemo demo = new CriteriaDemo();
        demo.start();
        constLayout.setOnClickListener((view) ->{
            Intent opretAnsat = new Intent(this, CreateEmployee.class); //TODO change to CreateEmplyee.class
            startActivity(opretAnsat);

        });

        lejeStart.setOnClickListener((view) ->{
            Date date = new Date();
            year = date.getYear() + 1900;
            month = date.getMonth();
            day = date.getDate();
            System.out.println("year: " + year + " Month: " + month + "day: " + day);
            showDialog(999);
        });

        lejeSlut.setOnClickListener((view) ->{
            Date date = new Date();
            year = date.getYear() + 1900;
            month = date.getMonth();
            day = date.getDate();
            showDialog(998);
        });

        //Load workers from database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(GlobalVariables.getFirebaseUser().getUid()+"/Medarbejdere");

        //Load employees and create cardviews and add to scroller
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot entry : snapshot.getChildren()){

                    //Parse JSON
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(entry.getValue().toString());
                    JsonObject obj = element.getAsJsonObject();


                    // 2. JSON to Java object, read it from a Json String.
                    CardView cv = new CardView(getApplicationContext());
                    cv.setId(Integer.parseInt(obj.get("ID").toString()));
                    LinearLayout.LayoutParams size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,225);
                    size.setMargins(0, 5, 0, 5);
                    cv.setLayoutParams(size);
                    cv.setRadius(15);
                    ConstraintLayout cl = new ConstraintLayout(getApplicationContext());
                    cv.addView(cl);
                    cv.setBackgroundResource(R.drawable.layout_background_round_corners);
                    cv.setOnClickListener((view) ->
                    {
                        //Opdaterer TextViews med information fra brugeren
                        //TODO Opdater alle informationer og ikke kun Erhverv, ID skal ikke vises og var kun et testforsøg.
                        //TODO Kan med fordel extractes til en metode.
                        Log.e("Test" , "Du trykkede på CardView " + cv.getId());
                        textViewErhverv.setText(obj.get("job").toString().replaceAll("\"", ""));
                        textViewLøn.setText(obj.get("pay").toString()+" kr/t");
                        textViewPostnummer.setText(obj.get("zipcode").toString());
                        textViewRadius.setText(obj.get("dist").toString()+" km");
                        for(int i = 0; i <  myContainer.getChildCount(); i++){
                            myContainer.getChildAt(i).setBackgroundResource(R.drawable.layout_background_round_corners);
                        }

                        view.setBackgroundResource(R.drawable.layout_background_round_corners_blue);

                        //If rental dates selected
                        if(textViewLejeperiodeSlut.getText().toString().contains("/") && textViewLejeperiodeStart.getText().toString().contains("/")) {
                            udlejBtn.setBackgroundResource(R.drawable.layout_background_round_corners_blue);
                        }
                        employeeSelected = true;

                    });
                    //Add pic
                    ImageView IVProfilePic = new ImageView(getApplicationContext());
                    //@SuppressLint("ResourceType") LinearLayout IVProfilePic = (LinearLayout) findViewById(R.drawable.circle);


                    IVProfilePic.setId(id++);
                    //IVProfilePic.setImageResource(R.drawable.circle);
                    //IVProfilePic.setBackground(R.drawable.roundimg);
                    //


                    //Set temporary picture while real pictures are downloading
                    IVProfilePic.setImageResource(R.drawable.download);


                    //We want to download images for the list of workers
                    new AsyncTask<Void, Void, Bitmap>(){
                        //Get pictures in background
                        @Override
                        protected Bitmap doInBackground(Void... voids) {
                            //IVProfilePic.setImageBitmap(getBitmapFromURL(obj.get("pic").toString()));
                            //System.out.println(obj.get("pic").toString().replace("\"", ""));
                            return getBitmapFromURL(obj.get("pic").toString().replace("\"", ""));
                        }

                        //On return update images in list
                        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
                        @Override
                        protected void onPostExecute(Bitmap s) {
                            super.onPostExecute(s);
                            IVProfilePic.setImageBitmap(s);
                        }
                    }.execute();

                    IVProfilePic.setAdjustViewBounds(true);
                    cl.addView(IVProfilePic);
                    //Add Name and Job
                    TextView TVName = new TextView(getApplicationContext());
                    TVName.setId(id++);
                    TVName.setText(obj.get("name").toString().replaceAll("\"", "")+"\n"+obj.get("job").toString().replaceAll("\"", ""));
                    TVName.setTextSize(15);

                    cl.addView(TVName);
                    //Add distance
                    //cv.setPadding(0,100,0,100);
                    //ReadMore

                    ConstraintSet CS = new ConstraintSet();
                    CS.clone(cl);
                    //Pic
                    CS.connect(IVProfilePic.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT,0);
                    CS.connect(IVProfilePic.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
                    //Name and Job
                    CS.connect(TVName.getId(), ConstraintSet.LEFT, IVProfilePic.getId(), ConstraintSet.RIGHT,8);
                    CS.connect(TVName.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
                    CS.connect(TVName.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);



                    CS.applyTo(cl);

                    myContainer.addView(cv);
                    //CrudEmployee staff = gson.fromJson(entry, );
                    //myContainer.addView(createNew(obj.get("name").toString(), obj.get("job").toString(), Double.parseDouble(obj.get("rank").toString()), Double.parseDouble(obj.get("pay").toString()), Integer.parseInt(obj.get("pic").toString())));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error!");
            }
        });

    }

    public ViewGroup.LayoutParams getLinearLayout(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                100
        );
        params.setMargins(0,15,75,0);
        return  params;
    }

    @Override
    public void onClick(View v)
    {

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
            textViewLejeperiodeStart.setText(Integer.toString(arg3)+"/"+Integer.toString(arg2+1)+"/"+Integer.toString(arg1));
            //If rental dates selected and employee is selected
            if(employeeSelected && textViewLejeperiodeSlut.getText().toString().contains("/") && textViewLejeperiodeStart.getText().toString().contains("/")) {
                udlejBtn.setBackgroundResource(R.drawable.layout_background_round_corners_blue);
            }
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            textViewLejeperiodeSlut.setText(Integer.toString(arg3)+"/"+Integer.toString(arg2+1)+"/"+Integer.toString(arg1));
            //If rental dates selected and employee is selected
            if(employeeSelected && textViewLejeperiodeSlut.getText().toString().contains("/") && textViewLejeperiodeStart.getText().toString().contains("/")) {
                udlejBtn.setBackgroundResource(R.drawable.layout_background_round_corners_blue);
            }
        }
    };

    public Bitmap getBitmapFromURL(String src) {
        try {
            //System.out.println(src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
