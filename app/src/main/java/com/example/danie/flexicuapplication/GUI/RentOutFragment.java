package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class RentOutFragment extends Fragment
    {
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
    private Button addEmployeeBtn;
    int id = 1;

    @SuppressLint("ResourceType")

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        View view = inflater.inflate(R.layout.fragment_rent_out, container, false);
        //Get intent and parse values
        //Intent intent = getIntent();
        callingActivity = "navigation";//intent.getStringExtra("callingActivity");

        //Setup loading bar and hide
     /*   loadingbar = view.findViewById(R.id.loadingbar);
        loadingbar.bringToFront();
        loadingbar.setVisibility(View.INVISIBLE);*/
        addEmployeeBtn = view.findViewById(R.id.addEmployee);
        LinearLayout myContainer = view.findViewById(R.id.scrollViewLayout2);


        addEmployeeBtn.setOnClickListener((vieww) -> {
        Intent opretAnsat = new Intent(getApplicationContext(), CreateEmployee.class); //TODO change to CreateEmplyee.class
        Bundle bndlanimation =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left, R.anim.anim_slide_out_left).toBundle();
        startActivity(opretAnsat, bndlanimation);

        });
        //Load workers from database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefUdlejid = database.getReference(GlobalVariables.getFirebaseUser().getUid() + "/Udlejninger");
        DatabaseReference myRefUdlejninger = database.getReference("Udlejninger");
        Gson gson = new Gson();

        //Load workers from database
        DatabaseReference myRefMedarbejder = database.getReference(GlobalVariables.getFirebaseUser().getUid() + "/Medarbejdere");

        //If employee has been created
        if (callingActivity.equals("createEmployeeFinish"))
            {
            System.out.println("Correct activity!");
            loadingbar.setVisibility(View.VISIBLE);
            ValueEventListener postListener = new ValueEventListener()
                {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                    {
                    for (DataSnapshot entry : dataSnapshot.getChildren())
                        {
                        // createEmployeeView(entry, myContainer);

                        }
                    }

                @Override
                public void onCancelled(DatabaseError databaseError)
                    {
                    System.out.println("Error!");
                    }
                };
            myRefMedarbejder.addValueEventListener(postListener);
            }

        //Load employees and create cardviews and add to scroller
        myRefMedarbejder.addListenerForSingleValueEvent(new ValueEventListener()
            {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onDataChange(DataSnapshot snapshot)
                {
                for (DataSnapshot entry : snapshot.getChildren())
                    {
                    // createEmployeeView(entry, myContainer);
                    createEmployeeNew(entry, myContainer);
                    }
                }

            private void createEmployeeNew(DataSnapshot entry, LinearLayout myContainer)
                {
                //Lav FindViewById på Viewsne som er blevet inflated
                View ExpandableCardview = getLayoutInflater().inflate(R.layout.employee_cardview, null, false);
                //View Spacer = getLayoutInflater().inflate(R.drawable.empty_tall_divider2, null, false);
                TextView textViewPay = ExpandableCardview.findViewById(R.id.textViewLøn);
                TextView textViewZipcode = ExpandableCardview.findViewById(R.id.textViewZipcode);
                TextView textViewDistance = ExpandableCardview.findViewById(R.id.textViewHeaderDistance);
                TextView textViewStatus = ExpandableCardview.findViewById(R.id.textViewHeaderStatus);
                LinearLayout linearLayoutCollapsed = ExpandableCardview.findViewById(R.id.linearLayoutCollapsed);
                LinearLayout linearLayoutExpanded = ExpandableCardview.findViewById(R.id.linearLayoutExpanded);
                ImageButton imageButtonArrow = ExpandableCardview.findViewById(R.id.imageButtonExpand);

                //Hent data og put det på TextViewsne
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(entry.getValue().toString());
                JsonObject Employee = element.getAsJsonObject();

                textViewPay.setText(Employee.get("pay").toString());
                textViewZipcode.setText(Employee.get("zipcode").toString());
                textViewDistance.setText(Employee.get("dist").toString());
                //textViewStatus.setText(Employee.get("available").toString());

                //Lav OnClickListener som håndterer at viewet bliver expanded og collapsed.
                linearLayoutCollapsed.setOnClickListener((test) ->
                {
                extract(linearLayoutExpanded, imageButtonArrow);
                });
                imageButtonArrow.setOnClickListener((test) ->
                {
                extract(linearLayoutExpanded, imageButtonArrow);
                });

                //Tilføjer det inflatede view til ScrollViewet
                myContainer.addView(ExpandableCardview);
                //myContainer.addView(Spacer);
                }

            private void extract(LinearLayout linearLayoutExpanded, ImageButton imageButtonArrow)
                {
                if (imageButtonArrow.getRotation() == -90)
                    {
                    linearLayoutExpanded.setVisibility(View.VISIBLE);
                    imageButtonArrow.setRotation(0);
                    }
                else if (imageButtonArrow.getRotation() == 0)
                    {
                    linearLayoutExpanded.setVisibility(View.GONE);
                    imageButtonArrow.setRotation(-90);
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError)
                {
                System.out.println("Error!");
                }
            });

        return view;

        }


    public ViewGroup.LayoutParams getLinearLayout()
        {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                100
        );
        params.setMargins(0, 15, 75, 0);
        return params;
        }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener()
        {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3)
            {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            textViewLejeperiodeStart.setText(Integer.toString(arg3) + "/" + Integer.toString(arg2 + 1) + "/" + Integer.toString(arg1));
            //If rental dates selected and employee is selected
            if (employeeSelected != 0 && textViewLejeperiodeSlut.getText().toString().contains("/") && textViewLejeperiodeStart.getText().toString().contains("/"))
                {
                addEmployeeBtn.setBackgroundResource(R.drawable.layout_background_round_corners_blue);
                }
            }
        };

    private DatePickerDialog.OnDateSetListener myDateListener2 = new DatePickerDialog.OnDateSetListener()
        {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3)
            {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            textViewLejeperiodeSlut.setText(Integer.toString(arg3) + "/" + Integer.toString(arg2 + 1) + "/" + Integer.toString(arg1));
            //If rental dates selected and employee is selected
            if (employeeSelected != 0 && textViewLejeperiodeSlut.getText().toString().contains("/") && textViewLejeperiodeStart.getText().toString().contains("/"))
                {
                addEmployeeBtn.setBackgroundResource(R.drawable.layout_background_round_corners_blue);
                }
            }
        };


    @SuppressLint("StaticFieldLeak")
    public void createEmployeeView(DataSnapshot entry, LinearLayout myContainer)
        {

        //Parse JSON
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(entry.getValue().toString());
        JsonObject obj = element.getAsJsonObject();

        int tempID = Integer.parseInt(String.valueOf(obj.get("ID")));
        if (existingViews.contains(tempID))
            {
            return;
            }

        existingViews.add(Integer.parseInt(String.valueOf(obj.get("ID"))));


        // 2. JSON to Java object, read it from a Json String.
        CardView cv = new CardView(getContext());
        cv.setId(Integer.parseInt(obj.get("ID").toString()));
        LinearLayout.LayoutParams size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 225);
        size.setMargins(0, 5, 0, 5);
        cv.setLayoutParams(size);
        cv.setRadius(15);
        @SuppressLint("RestrictedApi") ConstraintLayout cl = new ConstraintLayout(getContext());
        cv.addView(cl);
        cv.setBackgroundResource(R.drawable.layout_background_round_corners);
        cv.setOnClickListener((view) ->
        {
        //Opdaterer TextViews med information fra brugeren
        //TODO Opdater alle informationer og ikke kun Erhverv, ID skal ikke vises og var kun et testforsøg.
        //TODO Kan med fordel extractes til en metode.
        for (int i = 0; i < myContainer.getChildCount(); i++)
            {
            myContainer.getChildAt(i).setBackgroundResource(R.drawable.layout_background_round_corners);
            }

        view.setBackgroundResource(R.drawable.layout_background_round_corners_blue);

        });
        //Add pic
        @SuppressLint("RestrictedApi") ImageView IVProfilePic = new ImageView(getApplicationContext());


        IVProfilePic.setId(id++);

        if (obj.get("pic").toString().replaceAll("\"", "").equals("flexicu"))
            {

           /*if(loadingbar.getVisibility() == View.VISIBLE) {
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
            */
            //Get round image
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flexiculogocube);
            bitmap = RoundedImageView.getCroppedBitmap(bitmap, 200);
            IVProfilePic.setImageBitmap(bitmap);
            }
        else
            {
            //Set temporary picture while real pictures are downloading
            IVProfilePic.setImageResource(R.drawable.download);
            //We want to download images for the list of workers


            //System.out.println(src);
            URL url = null;
            try
                {
                url = new URL(obj.get("pic").toString().replace("\"", ""));
                } catch (MalformedURLException e)
                {
                e.printStackTrace();
                }

            //We want to download images for the list of workers
            URL finalUrl = url;
            new AsyncTask<Void, Void, Bitmap>()
                {
                //Get pictures in background
                @Override
                protected Bitmap doInBackground(Void... voids)
                    {
                    try
                        {
                        //Use glide for faster load and to save images in cache! (glide.asBitmap does not create its own asynctask)
                        Bitmap myBitmap = Glide
                                .with(IVProfilePic)
                                .asBitmap()
                                .load(finalUrl)
                                .submit()
                                .get();
                        return myBitmap;
                        } catch (ExecutionException e)
                        {
                        e.printStackTrace();
                        } catch (InterruptedException e)
                        {
                        e.printStackTrace();
                        }
                    return null;
                    }

                //On return update images in list
                @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
                @Override
                protected void onPostExecute(Bitmap s)
                    {
                    super.onPostExecute(s);
                    s = RoundedImageView.getCroppedBitmap(s, 200);
                    IVProfilePic.setImageBitmap(s);
                 /*   if(loadingbar.getVisibility() == View.VISIBLE) {
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
                    }*/

                    }
                }.execute();



           /* if(loadingbar.getVisibility() == View.VISIBLE) {
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
            }*/
            }

        IVProfilePic.setAdjustViewBounds(true);
        cl.addView(IVProfilePic);
        //Add Name and Job
        @SuppressLint("RestrictedApi") TextView TVName = new TextView(getApplicationContext());
        TVName.setId(id++);
        TVName.setText(obj.get("name").toString().replaceAll("\"", "") + "\n" + obj.get("job").toString().replaceAll("\"", ""));
        TVName.setTextSize(15);

        cl.addView(TVName);

        ConstraintSet CS = new ConstraintSet();
        CS.clone(cl);
        //Pic
        CS.connect(IVProfilePic.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 15);
        CS.connect(IVProfilePic.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        CS.connect(IVProfilePic.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

        //Name and Job
        CS.connect(TVName.getId(), ConstraintSet.LEFT, IVProfilePic.getId(), ConstraintSet.RIGHT, 8);
        CS.connect(TVName.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        CS.connect(TVName.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        CS.connect(TVName.getId(), ConstraintSet.LEFT, IVProfilePic.getId(), ConstraintSet.LEFT, 250);

        CS.applyTo(cl);

        myContainer.addView(cv);
        //CrudEmployee staff = gson.fromJson(entry, );
        //myContainer.addView(createNew(obj.get("name").toString(), obj.get("job").toString(), Double.parseDouble(obj.get("rank").toString()), Double.parseDouble(obj.get("pay").toString()), Integer.parseInt(obj.get("pic").toString())));
        }
    }
