package com.example.danie.flexicuapplication;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Indlej extends AppCompatActivity implements View.OnClickListener {

    int id = 1;
    LinearLayout scroller;
    ImageView filterMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indlej);

        scroller = findViewById(R.id.linearLayout);
        filterMenu = findViewById(R.id.filterMenu);

        filterMenu.setOnClickListener(this);

        createNew("mathias", "Java",4.2, 250.);

        }
    public void createNew(String name, String job, double rank, Double pay){
        CardView cv = new CardView(getApplicationContext());
        cv.setId(id++);
        LinearLayout.LayoutParams size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,225);
        cv.setLayoutParams(size);
        cv.setRadius(15);
        ConstraintLayout cl = new ConstraintLayout(this);
        cv.addView(cl);
        //Add pic
        ImageView IVProfilePic = new ImageView(this);
        IVProfilePic.setId(id++);
        IVProfilePic.setImageResource(R.drawable.download);
        IVProfilePic.setAdjustViewBounds(true);
        cl.addView(IVProfilePic);
        //Add Name and Job
        TextView TVName = new TextView(this);
        TVName.setId(id++);
        TVName.setText(name+"\n"+job);
        TVName.setTextSize(18);

        cl.addView(TVName);
        //Add Rank
        ImageView IVRank = new ImageView(this);
        IVRank.setId(id++);
        IVRank.setImageResource(R.drawable.blue_star_icon);
        IVRank.setAdjustViewBounds(true);
        IVRank.setMaxHeight(150);
        IVRank.setMaxWidth(150);
        TextView TVRank = new TextView(this);
        TVRank.setId(id++);
        TVRank.setText(Double.toString(rank));
        TVRank.setTextSize(18);
        cl.addView(IVRank);
        cl.addView(TVRank);
        //Add pay
        TextView TVPay = new TextView(this);
        TVPay.setId(id++);
        TVPay.setTextSize(22);
        TVPay.setText(Double.toString(pay));
        TextView TVPayConst = new TextView(this);
        TVPayConst.setId(id++);
        TVPayConst.setTextSize(14);
        TVPayConst.setText("Timeløn");
        cl.addView(TVPay);
        cl.addView(TVPayConst);
        //Add distance
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
        //Rank
        CS.connect(IVRank.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
        CS.connect(IVRank.getId(), ConstraintSet.LEFT, TVName.getId(), ConstraintSet.RIGHT,0);
        CS.connect(TVRank.getId(), ConstraintSet.TOP, IVRank.getId(), ConstraintSet.BOTTOM,0);
        CS.connect(TVRank.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,8);
        CS.connect(TVRank.getId(), ConstraintSet.LEFT, IVRank.getId(), ConstraintSet.LEFT,0);
        CS.connect(TVRank.getId(), ConstraintSet.RIGHT, IVRank.getId(), ConstraintSet.RIGHT,0);
        //Pay
        CS.connect(TVPayConst.getId(), ConstraintSet.LEFT, TVPay.getId(), ConstraintSet.LEFT,0);
        CS.connect(TVPayConst.getId(), ConstraintSet.TOP, TVRank.getId(), ConstraintSet.TOP,0);
        CS.connect(TVPay.getId(), ConstraintSet.BOTTOM, TVPayConst.getId(), ConstraintSet.TOP,0);
        CS.connect(TVPay.getId(), ConstraintSet.LEFT, IVRank.getId(), ConstraintSet.RIGHT,0);



        CS.applyTo(cl);



    scroller.addView(cv);
    }

    @Override
    public void onClick(View v) {

    }
}
