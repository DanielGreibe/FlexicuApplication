package com.example.danie.flexicuapplication;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineIndlejninger extends AppCompatActivity {

    int id = 1;
    LinearLayout scroller;
    ConstraintLayout mainLayout;
    TextView mainText;


    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_indlejninger);

        scroller = findViewById(R.id.linearlayout1);

        mainLayout = findViewById(R.id.MineIndlejninger_mainLayout);
        mainText = findViewById(R.id.textView2);

        for(int i = 0; i<20; i++)
        createNew("Mathias", "Java",4.2, 250., R.drawable.mathias);

        /*ConstraintLayout firstEmployee = findViewById(R.id.firstEmployee);
        ConstraintLayout firstEmployee2 = findViewById(R.id.firstEmployee2);
        ConstraintLayout firstEmployee3 = findViewById(R.id.firstEmployee3);
        ConstraintLayout firstEmployee4 = findViewById(R.id.firstEmployee4);
        ConstraintLayout firstEmployee5 = findViewById(R.id.firstEmployee5);
        ConstraintLayout firstEmployee6 = findViewById(R.id.firstEmployee6);
        ConstraintLayout firstEmployee7 = findViewById(R.id.firstEmployee7);
        ConstraintLayout firstEmployee8 = findViewById(R.id.firstEmployee8);
        ConstraintLayout firstEmployee9 = findViewById(R.id.firstEmployee9);
        ConstraintLayout firstEmployee10 = findViewById(R.id.firstEmployee10);
        ConstraintLayout firstEmployee11 = findViewById(R.id.firstEmployee11);
        ConstraintLayout firstEmployee12 = findViewById(R.id.firstEmployee12);
        ConstraintLayout firstEmployee13 = findViewById(R.id.firstEmployee13);
        ConstraintLayout firstEmployee14 = findViewById(R.id.firstEmployee14);
        ConstraintLayout firstEmployee15 = findViewById(R.id.firstEmployee15);
        ConstraintLayout firstEmployee16 = findViewById(R.id.firstEmployee16);
        ConstraintLayout firstEmployee17 = findViewById(R.id.firstEmployee17);
        ConstraintLayout firstEmployee18 = findViewById(R.id.firstEmployee18);
        ConstraintLayout firstEmployee19 = findViewById(R.id.firstEmployee19);
        ConstraintLayout firstEmployee20 = findViewById(R.id.firstEmployee20);

        ImageView profileImage2 = findViewById(R.id.profile_image2);
        TextView nameBox2 = findViewById(R.id.nameBox2);

        nameBox2.setText("Oliver");
        profileImage2.setImageResource(R.drawable.oliver);




        scroller.removeView(firstEmployee);
        scroller.addView(firstEmployee);*/

//Listview og __view
        //Over i XML-fil


        //mainLayout.addView(myButton);
        //mainLayout.addView(myConstraint);

        }

    public void createNew(String name, String job, double rank, Double pay, int Drawable){
        CardView cv = new CardView(getApplicationContext());
        cv.setId(id++);
        LinearLayout.LayoutParams size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,225);
        size.setMargins(0, 5, 0, 5);
        cv.setLayoutParams(size);
        cv.setRadius(15);
        ConstraintLayout cl = new ConstraintLayout(this);
        cv.addView(cl);
        //Add pic
        ImageView IVProfilePic = new ImageView(this);
        IVProfilePic.setId(id++);
        //IVProfilePic.setBackground(R.drawable.roundimg);
        IVProfilePic.setImageResource(R.drawable.daniel);
        IVProfilePic.setAdjustViewBounds(true);
        cl.addView(IVProfilePic);
        //Add Name and Job
        TextView TVName = new TextView(this);
        TVName.setId(id++);
        TVName.setText(name+"\n"+job);
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



        scroller.addView(cv);
    }
    }
