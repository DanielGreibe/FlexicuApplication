package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RentOut extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private ConstraintLayout constLayout;
    private ConstraintLayout constCardLayout;
    TextView textViewErhverv, textViewLejeperiodeStart;
    private ConstraintLayout udlejBtn;
    int id = 1;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_out);
        textViewErhverv = findViewById(R.id.erhvevtxt);
        textViewLejeperiodeStart = findViewById(R.id.lejeStart);
        mContext = getApplicationContext();
        LinearLayout myContainer = findViewById(R.id.scrollLayoutUdlej);
        constLayout = findViewById(R.id.opretMedarbejder);
        udlejBtn = findViewById(R.id.UdlejBtn);
        CriteriaDemo demo = new CriteriaDemo();
        demo.start();
        constLayout.setOnClickListener((view) ->{
            Intent opretAnsat = new Intent(this, CreateEmployee.class);
            startActivity(opretAnsat);

        });

        //Load workers from database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(GlobalVariables.getFirebaseUser().getUid()+"/Medarbejdere");

        //Check for existing ID.
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot entry : snapshot.getChildren()){

                    //Parse JSON
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(entry.getValue().toString());
                    JsonObject obj = element.getAsJsonObject();


                    System.out.println("ID IS "+obj.get("ID"));

                    // 2. JSON to Java object, read it from a Json String.
                    CardView cv = new CardView(getApplicationContext());
                    cv.setId(Integer.parseInt(obj.get("ID").toString()));
                    LinearLayout.LayoutParams size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,225);
                    size.setMargins(0, 5, 0, 5);
                    cv.setLayoutParams(size);
                    cv.setRadius(15);
                    ConstraintLayout cl = new ConstraintLayout(getApplicationContext());
                    cv.addView(cl);
                    cv.setOnClickListener((view) ->
                    {
                        //Opdaterer TextViews med information fra brugeren
                        //TODO Opdater alle informationer og ikke kun Erhverv, ID skal ikke vises og var kun et testforsøg.
                        //TODO Kan med fordel extractes til en metode.
                        Log.e("Test" , "Du trykkede på CardView " + cv.getId());
                        textViewLejeperiodeStart.setText("ID: " + obj.get("ID").toString());
                        textViewErhverv.setText(obj.get("job").toString().replaceAll("\"", ""));
                        for(int i = 0; i <  myContainer.getChildCount(); i++){
                            myContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
                        }

                        view.setBackgroundColor(Color.rgb(0,153,203));
                        udlejBtn.setBackgroundResource(R.drawable.layout_background_round_corners_blue);

                    });
                    //Add pic
                    ImageView IVProfilePic = new ImageView(getApplicationContext());
                    //@SuppressLint("ResourceType") LinearLayout IVProfilePic = (LinearLayout) findViewById(R.drawable.circle);


                    IVProfilePic.setId(id++);
                    //IVProfilePic.setImageResource(R.drawable.circle);
                    //IVProfilePic.setBackground(R.drawable.roundimg);
                    //
                    IVProfilePic.setImageResource(R.drawable.download);
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

    public void setinfo(CardView CV){

    }

    public TextView addTextView(TextView TV){
        TV.setLayoutParams(getLinearLayout());
        TV.setText("Thomas - snedker");
        TV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        TV.setTextColor(Color.BLACK);
        TV.setPadding(125,0,0,0);

        return TV;
    }

    public ViewGroup.LayoutParams getLinearLayout(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                100
        );
        params.setMargins(0,15,75,0);
        return  params;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    public ConstraintLayout createNew(String name, String erhverv, double rank, Double pay, int Drawable){
        CardView cv = new CardView(getApplicationContext());
        cv.setId(id++);
        LinearLayout.LayoutParams size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,225);
        size.setMargins(0, 5, 0, 5);
        cv.setLayoutParams(size);
        cv.setRadius(15);
        ConstraintLayout cl = new ConstraintLayout(this);
        cv.addView(cl);
        cv.setOnClickListener(this);
        //Add pic
        ImageView IVProfilePic = new ImageView(this);
        //@SuppressLint("ResourceType") LinearLayout IVProfilePic = (LinearLayout) findViewById(R.drawable.circle);


        IVProfilePic.setId(id++);
        //IVProfilePic.setImageResource(R.drawable.circle);
        //IVProfilePic.setBackground(R.drawable.roundimg);
        //
        IVProfilePic.setImageResource(R.drawable.download);
        IVProfilePic.setAdjustViewBounds(true);
        cl.addView(IVProfilePic);
        //Add Name and Job
        TextView TVName = new TextView(this);
        TVName.setId(id++);
        TVName.setText(name+"\n"+erhverv);
        TVName.setTextSize(18);

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


        return cl;
    }

    @Override
    public void onClick(View v)
    {

    }
}
