package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.danie.flexicuapplication.LogicLayer.CrudEmployee;
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

    //Visual logic
    int employeeSelected = 0;
    String callingActivity = "default";
    ArrayList<String> existingViews = new ArrayList<>();

    TextView loadingbar, textViewLejeperiodeStart, textViewLejeperiodeSlut;
    private Button addEmployeeBtn;
    int id = 1;

    @SuppressLint("ResourceType")

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent_out, container, false);
        //Get intent and parse values
        if(getActivity().getIntent().getStringExtra("createEmployeeFinish") != null){
            callingActivity = getActivity().getIntent().getStringExtra("createEmployeeFinish");
        }
        onActivityCreated(savedInstanceState);
        //Setup loading bar and hide
        loadingbar = view.findViewById(R.id.loadingbarTextView);
        loadingbar.bringToFront();
        loadingbar.setVisibility(View.INVISIBLE);
        addEmployeeBtn = view.findViewById(R.id.addEmployee);
        LinearLayout myContainer = view.findViewById(R.id.scrollViewLayout2);
        addEmployeeBtn.setOnClickListener((vieww) -> {
        Intent opretAnsat = new Intent(getApplicationContext(), CreateEmployee.class); //TODO change to CreateEmplyee.class
            opretAnsat.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Bundle bndlanimation =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left, R.anim.anim_slide_out_left).toBundle();
        startActivity(opretAnsat, bndlanimation);
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Load workers from database
        DatabaseReference myRefMedarbejder = database.getReference("Users/"+GlobalVariables.getFirebaseUser().getUid() + "/Medarbejdere");

        //If employee has been created
        if (callingActivity.equals("createEmployeeFinish")) {
            loadingbar.setVisibility(View.VISIBLE);
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("HEREHERE " + String.valueOf(getActivity()).contains("TabbedRentIn"));
                    if(!String.valueOf(getActivity()).contains("TabbedRentIn")){
                        return;
                    }
                    for (DataSnapshot entry : dataSnapshot.getChildren()){
                            createNewEmployee(entry, myContainer);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError){
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
                    createNewEmployee(entry, myContainer);
                    }
                }



            @Override
            public void onCancelled(DatabaseError databaseError)
                {
                System.out.println("Error!");
                }
            });


        if(callingActivity.equals("createEmployeeFinish")){
            loadingbar.setVisibility(View.VISIBLE);
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("HEREHERE2 " + String.valueOf(getActivity()).contains("TabbedRentIn"));
                    if(!String.valueOf(getActivity()).contains("TabbedRentIn")){
                        return;
                    }
                    for(DataSnapshot entry : dataSnapshot.getChildren()){
                        createNewEmployee(entry, myContainer);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Error!");
                }
            };
            myRefMedarbejder.addValueEventListener(postListener);
        }

        return view;

        }


    @SuppressLint("StaticFieldLeak")
    public void createNewEmployee(DataSnapshot entry, LinearLayout myContainer){

        //Hent data og gør det til et JsonObject
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(entry.getValue().toString());
        JsonObject Employee = element.getAsJsonObject();

        String tempID = Employee.get("ID").toString().replaceAll("\"","");
        if(existingViews.contains(tempID)){
            return;
        }
        //Inflater to XML filer ind, et Cardview og en Spacer som bruges til at skabe afstand fordi det ikke er muligt med Padding eller Layout Margin.
        View ExpandableCardview = getLayoutInflater().inflate(R.layout.employee_cardview, null, false);
        View Spacer = getLayoutInflater().inflate(R.layout.spacer, null, false);

        //Lav FindViewById på Viewsne som er blevet inflated
        TextView textViewPay = ExpandableCardview.findViewById(R.id.textViewLøn);
        TextView textViewZipcode = ExpandableCardview.findViewById(R.id.textViewZipcode);
        TextView textViewDistance = ExpandableCardview.findViewById(R.id.textViewDistance);
        TextView textViewStatus = ExpandableCardview.findViewById(R.id.textViewStatus);
        LinearLayout linearLayoutCollapsed = ExpandableCardview.findViewById(R.id.linearLayoutCollapsed);
        LinearLayout linearLayoutExpanded = ExpandableCardview.findViewById(R.id.linearLayoutExpanded);
        ImageView imageButtonArrow = ExpandableCardview.findViewById(R.id.imageButtonExpand);
        TextView textViewName = ExpandableCardview.findViewById(R.id.textViewName);
        TextView textViewProfession = ExpandableCardview.findViewById(R.id.textViewProfession);
        ImageView profilePic = ExpandableCardview.findViewById(R.id.imageViewImage);
        Button udlejBtn = ExpandableCardview.findViewById(R.id.buttonUdlej);
        TextView textViewLejeperiodeStart = ExpandableCardview.findViewById(R.id.textViewLejeperiodeStart);
        TextView textViewLejeperiodeSlut = ExpandableCardview.findViewById(R.id.textViewLejeperiodeSlut);
        TextView textViewDescription = ExpandableCardview.findViewById(R.id.textViewDescription);

        //Træk data ud af Json Objektet og put det på textviews i Cardviewet.
        textViewPay.setText(Employee.get("pay").toString() + " kr/t");
        textViewZipcode.setText(Employee.get("zipcode").toString());
        textViewDistance.setText(Employee.get("dist").toString() + " km");
        textViewName.setText(Employee.get("name").toString().replace("\"" , ""));
        textViewProfession.setText(Employee.get("job").toString().replace("\"" , ""));
        textViewStatus.setText(Employee.get("status").toString().replaceAll("\"",""));

        if (Employee.get("status").toString().replace("\"", "").equals("ikke udlejet") || Employee.get("status").toString().replace("\"", "").equals("sat til udleje"))
            {
            textViewLejeperiodeStart.setText("Ikke udlejet");
            textViewLejeperiodeSlut.setText("Ikke udlejet");
            }
    else
            {
            //TODO sæt textViweLejeperiodeStart og textViewLejeperiodeSlut til rigtig data
            }



        //Tjek status
        if(!Employee.get("status").toString().replaceAll("\"", "").equals("ikke udlejet")){
            udlejBtn.setEnabled(false);
            udlejBtn.setBackgroundColor(Color.GRAY);
        }


        //Set temporary picture while real pictures are downloading
            profilePic.setImageResource(R.drawable.download);
            //We want to download images for the list of workers

        //Set calendar onClick listeners
        udlejBtn.setOnClickListener((view) ->{
            Intent intent = new Intent(this.getContext(), rentOut1.class);
            intent.putExtra("entryString", entry.getValue().toString());
            startActivity(intent);
            getActivity().finish();

        });

            //System.out.println(src);
            URL url = null;
            try {
                url = new URL(Employee.get("pic").toString().replace("\"", ""));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }



            if(Employee.get("pic").toString().replace("\"", "").equals("flexicu")){
                int minPixels = 0;
                Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.flexiculogocube);
                if(photo.getWidth() < photo.getHeight()){
                    minPixels = photo.getWidth();
                }
                else {
                    minPixels = photo.getHeight();
                    Bitmap squareImg = Bitmap.createBitmap(photo, ((photo.getWidth() - minPixels) / 2), ((photo.getHeight() - minPixels) / 2), minPixels, minPixels);
                    squareImg = RoundedImageView.getCroppedBitmap(squareImg, 400);
                    profilePic.setImageBitmap(squareImg);
                }
            } else {
                //We want to download images for the list of workers
                URL finalUrl = url;
                new AsyncTask<Void, Void, Bitmap>() {
                    //Get pictures in background
                    @Override
                    protected Bitmap doInBackground(Void... voids) {
                        try {
                            //Use glide for faster load and to save images in cache! (glide.asBitmap does not create its own asynctask)
                            Bitmap myBitmap = Glide
                                    .with(profilePic)
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
                        profilePic.setImageBitmap(s);
                        if (loadingbar.getVisibility() == View.VISIBLE) {
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
            }

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
        //Lav OnClickListener som håndterer at viewet bliver expanded og collapsed.
        linearLayoutCollapsed.setOnClickListener((test) ->
        {

        expand(linearLayoutExpanded, imageButtonArrow);
        });
        imageButtonArrow.setOnClickListener((test) ->
        {
        expand(linearLayoutExpanded, imageButtonArrow);
        });

        //Tilføjer Cardviewet og Spaceren til det Linære Layout myContainer.
        myContainer.addView(ExpandableCardview);
        myContainer.addView(Spacer);
        existingViews.add(Employee.get("ID").toString().replaceAll("\"",""));
        }

    private void expand(LinearLayout linearLayoutExpanded, ImageView imageButtonArrow)
        {
        if (linearLayoutExpanded.getVisibility() == View.GONE)
            {
            linearLayoutExpanded.setVisibility(View.VISIBLE);
            imageButtonArrow.setRotation(90);
            }
        else if (linearLayoutExpanded.getVisibility() == View.VISIBLE)
            {
            linearLayoutExpanded.setVisibility(View.GONE);
            imageButtonArrow.setRotation(0);
            }
        }
    }
