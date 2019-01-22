package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaDemo;
import com.example.danie.flexicuapplication.LogicLayer.CrudRentOut;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.LogicLayer.RoundedImageView;
import com.example.danie.flexicuapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class RentOut extends AppCompatActivity implements View.OnClickListener {
    //Date picker variables
    private Calendar calendar;
    private int year, month, day;

    //Visual logic
    int employeeSelected = 0;
    String callingActivity = "default";
    ArrayList<Integer> existingViews = new ArrayList<>();

    //Storage ref
    private StorageReference mStorageRef;


    private Context mContext;
    private ConstraintLayout opretMedarbejderButton;
    private ConstraintLayout lejeStart, lejeSlut, loadingbar;
    private ConstraintLayout constCardLayout;
    TextView textViewErhverv, textViewLejeperiodeStart, textViewRadius, textViewPostnummer, textViewLøn, textViewLejeperiodeSlut;
    private ConstraintLayout udlejBtn;
    int id = 1;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_out);

        //Get intent and parse values
        Intent intent = getIntent();
        callingActivity = intent.getStringExtra("callingActivity");

        lejeSlut = findViewById(R.id.lejeSlut);
        lejeStart = findViewById(R.id.lejeStart);

        //Setup loading bar and hide
        loadingbar = findViewById(R.id.loadingbar);
        loadingbar.bringToFront();
        loadingbar.setVisibility(View.INVISIBLE);


        textViewRadius = findViewById(R.id.radiusTextView);
        textViewPostnummer = findViewById(R.id.postnummerTextView);
        textViewLøn = findViewById(R.id.lønTextView);
        textViewLejeperiodeSlut = findViewById(R.id.lejeSlutTextView);
        textViewErhverv = findViewById(R.id.erhvervTextView);
        textViewLejeperiodeStart = findViewById(R.id.lejeStartTextView);
        mContext = getApplicationContext();
        LinearLayout myContainer = findViewById(R.id.scrollLayoutUdlej);
        opretMedarbejderButton = findViewById(R.id.opretMedarbejder);
        udlejBtn = findViewById(R.id.UdlejBtn);


        opretMedarbejderButton.setOnClickListener((view) ->{
            Intent opretAnsat = new Intent(this, CreateEmployee.class); //TODO change to CreateEmplyee.class
            Bundle bndlanimation =
                    ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left, R.anim.anim_slide_out_left).toBundle();
            startActivity(opretAnsat, bndlanimation);

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
        DatabaseReference myRefUdlejid = database.getReference("Users/"+GlobalVariables.getFirebaseUser().getUid()+"/Udlejninger");
        DatabaseReference myRefUdlejninger = database.getReference("Udlejninger");
        Gson gson = new Gson();


        udlejBtn.setOnClickListener((view) -> {
            if(employeeSelected != 0
                    && textViewLejeperiodeSlut.getText().toString().contains("/")
                    && textViewLejeperiodeStart.getText().toString().contains("/")){
                DatabaseReference myRefId = database.getReference("Users/"+GlobalVariables.getFirebaseUser().getUid() + "/Medarbejdere/" + employeeSelected);
                myRefId.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //Parse JSON
                        JsonParser parser = new JsonParser();
                        JsonElement element = parser.parse(snapshot.getValue().toString());
                        JsonObject obj = element.getAsJsonObject();

                        String tempName = obj.get("name").toString().replaceAll("\"", "");
                        String tempJob = obj.get("job").toString().replaceAll("\"", "");
                        String tempPic = obj.get("pic").toString().replaceAll("\"", "");
                        String tempRank = obj.get("rank").toString().replaceAll("\"", "");
                        String tempPay = obj.get("pay").toString().replaceAll("\"", "");
                        String tempOwner = obj.get("owner").toString().replaceAll("\"", "");

                        CrudRentOut newRentOut = new CrudRentOut(Integer.toString(employeeSelected), tempName, tempJob, tempPic, textViewLejeperiodeStart.getText().toString(), textViewLejeperiodeSlut.getText().toString(), tempRank, tempPay, "2850", 25, tempOwner, "asd");
                        String rentOutJSON = gson.toJson(newRentOut);
                        myRefUdlejninger.child(Integer.toString(newRentOut.getRentId())).setValue(rentOutJSON);
                        String rentOutIdJSON = gson.toJson("Users/"+ GlobalVariables.getFirebaseUser().getUid() + employeeSelected);
                        myRefUdlejid.child(Integer.toString(newRentOut.getRentId())).setValue(rentOutIdJSON);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("Error!");
                    }
                });
            }
        });

        //Load workers from database
        DatabaseReference myRefMedarbejder = database.getReference("Users/"+GlobalVariables.getFirebaseUser().getUid()+"/Medarbejdere");

        //If employee has been created

        if(callingActivity.equals("createEmployeeFinish")){
            System.out.println("Correct activity!");
            loadingbar.setVisibility(View.VISIBLE);
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot entry : dataSnapshot.getChildren()){
                        createEmployeeView(entry, myContainer);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Error!");
                }
            };
            myRefMedarbejder.addValueEventListener(postListener);
        }

        //Load employees and create cardviews and add to scroller
        myRefMedarbejder.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot entry : snapshot.getChildren()) {
                    createEmployeeView(entry, myContainer);
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
            if(employeeSelected != 0 && textViewLejeperiodeSlut.getText().toString().contains("/") && textViewLejeperiodeStart.getText().toString().contains("/")) {
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
            if(employeeSelected != 0 && textViewLejeperiodeSlut.getText().toString().contains("/") && textViewLejeperiodeStart.getText().toString().contains("/")) {
                udlejBtn.setBackgroundResource(R.drawable.layout_background_round_corners_blue);
            }
        }
    };

    public Bitmap getBitmapFromURL(String src) {
        try {
            //System.out.println(src);
            URL url = new URL(src);
            /*HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();*/

            ImageView temp = null;
            Glide.with(this)
                    .load(url)
                    .into(temp);


            Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), temp.getId());
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void createEmployeeView(DataSnapshot entry, LinearLayout myContainer){

        //Parse JSON
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(entry.getValue().toString());
        JsonObject obj = element.getAsJsonObject();

        int tempID = Integer.parseInt(String.valueOf(obj.get("ID")));
        if(existingViews.contains(tempID)){
            return;
        }

        existingViews.add(Integer.parseInt(String.valueOf(obj.get("ID"))));


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
            employeeSelected = tempID;

        });
        //Add pic
        ImageView IVProfilePic = new ImageView(getApplicationContext());
        //@SuppressLint("ResourceType") LinearLayout IVProfilePic = (LinearLayout) findViewById(R.drawable.circle);


        IVProfilePic.setId(id++);
        //IVProfilePic.setImageResource(R.drawable.circle);
        //IVProfilePic.setBackground(R.drawable.roundimg);
        //

        if(obj.get("pic").toString().replaceAll("\"", "").equals("flexicu")){

            if(loadingbar.getVisibility() == View.VISIBLE) {
                //Set fade animation and hide after animation end
                AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(1500);
                anim.setRepeatCount(0);
                anim.willChangeBounds();
                loadingbar.startAnimation(anim);
                loadingbar.postOnAnimation(new Runnable() {
                    @Override
                    public void run() {
                        loadingbar.setVisibility(View.INVISIBLE);
                    }
                });
            }
            //Get round image
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flexiculogocube);
            bitmap = RoundedImageView.getCroppedBitmap(bitmap, 200);
            IVProfilePic.setImageBitmap(bitmap);
        }else{
            //Set temporary picture while real pictures are downloading
            IVProfilePic.setImageResource(R.drawable.download);
            //We want to download images for the list of workers

            //Bitmap s = getBitmapFromURL(obj.get("pic").toString().replace("\"", ""));

            //System.out.println(src);
            URL url = null;
            try {
                url = new URL(obj.get("pic").toString().replace("\"", ""));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            /*HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();*/

            //We want to download images for the list of workers
            URL finalUrl = url;
            new AsyncTask<Void, Void, Bitmap>(){
                //Get pictures in background
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    try {
                        //Use glide for faster load and to save images in cache! (glide.asBitmap does not create its own asynctask)
                        Bitmap myBitmap = Glide
                                .with(IVProfilePic)
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
                    IVProfilePic.setImageBitmap(s);
                    if(loadingbar.getVisibility() == View.VISIBLE) {
                        //Set fade animation and hide after animation end
                        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
                        anim.setDuration(1500);
                        anim.setRepeatCount(0);
                        anim.willChangeBounds();
                        loadingbar.startAnimation(anim);
                        loadingbar.postOnAnimation(new Runnable() {
                            @Override
                            public void run() {
                                loadingbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
            }.execute();



            if(loadingbar.getVisibility() == View.VISIBLE) {
                //Set fade animation and hide after animation end
                AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(1500);
                anim.setRepeatCount(0);
                anim.willChangeBounds();
                loadingbar.startAnimation(anim);
                loadingbar.postOnAnimation(new Runnable() {
                    @Override
                    public void run() {
                        loadingbar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }

        IVProfilePic.setAdjustViewBounds(true);
        cl.addView(IVProfilePic);
        //Add Name and Job
        TextView TVName = new TextView(getApplicationContext());
        TVName.setId(id++);
        TVName.setText(obj.get("name").toString().replaceAll("\"", "")+"\n"+obj.get("job").toString().replaceAll("\"", ""));
        TVName.setTextSize(15);

        cl.addView(TVName);

        ConstraintSet CS = new ConstraintSet();
        CS.clone(cl);
        //Pic
        CS.connect(IVProfilePic.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT,15);
        CS.connect(IVProfilePic.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
        CS.connect(IVProfilePic.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);

        //Name and Job
        CS.connect(TVName.getId(), ConstraintSet.LEFT, IVProfilePic.getId(), ConstraintSet.RIGHT,8);
        CS.connect(TVName.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
        CS.connect(TVName.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);
        CS.connect(TVName.getId(), ConstraintSet.LEFT, IVProfilePic.getId(), ConstraintSet.LEFT, 250);

        CS.applyTo(cl);

        myContainer.addView(cv);
        //CrudEmployee staff = gson.fromJson(entry, );
        //myContainer.addView(createNew(obj.get("name").toString(), obj.get("job").toString(), Double.parseDouble(obj.get("rank").toString()), Double.parseDouble(obj.get("pay").toString()), Integer.parseInt(obj.get("pic").toString())));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
        Intent intent = new Intent(this, Navigation.class);
        intent.putExtra("callingActivity", "navigation");
        finish();
    }
}
