package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class RentOutFragment extends Fragment
    {
    //Visual logic
    int employeeSelected = 0;
    String callingActivity = "default";
    ArrayList<String> existingViews = new ArrayList<>();
    LinearLayout myContainer;
    boolean allredyRentOut;

    TextView loadingbar, textViewLejeperiodeStart, textViewLejeperiodeSlut;
    private FloatingActionButton addEmployeeBtn;
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
        myContainer = view.findViewById(R.id.scrollViewLayout2);
        View cardDevider = getLayoutInflater().inflate(R.layout.card_devider, null, false);
        myContainer.addView(cardDevider);
        cardDevider.setVisibility(View.INVISIBLE);
        addEmployeeBtn.setOnClickListener((vieww) -> {
        Intent opretAnsat = new Intent(getApplicationContext(), CreateEmployee.class); //TODO change to CreateEmplyee.class
            opretAnsat.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Bundle bndlanimation =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left, R.anim.anim_slide_out_left).toBundle();
        startActivity(opretAnsat, bndlanimation);
        //getActivity().finish();
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
                    if(!String.valueOf(getActivity()).contains("TabbedRentOut")){
                        return;
                    }
                    for (DataSnapshot entry : dataSnapshot.getChildren()){
                            createNewEmployee(entry, myContainer);
                    }
                    if(allredyRentOut) {
                        cardDevider.setVisibility(View.VISIBLE);
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
                    if(allredyRentOut) {
                        cardDevider.setVisibility(View.VISIBLE);
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
                    if(!String.valueOf(getActivity()).contains("TabbedRentOut")){
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
        TextView headerDescription = ExpandableCardview.findViewById(R.id.textViewHeaderDescription);
        TextView textViewDescription = ExpandableCardview.findViewById(R.id.textViewDescription);

        //Træk data ud af Json Objektet og put det på textviews i Cardviewet.
        textViewPay.setText(Employee.get("pay").toString() + " kr/t");
        textViewZipcode.setText(Employee.get("zipcode").toString());
        textViewDistance.setText(Employee.get("dist").toString() + " km");
        textViewName.setText(Employee.get("name").toString().replace("\"" , ""));
        textViewProfession.setText(Employee.get("job").toString().replace("\"" , ""));
        textViewStatus.setText(Employee.get("status").toString().replaceAll("\"",""));

        if(Employee.get("description").toString().replace("\"" , "").equals("")){
            headerDescription.setVisibility(View.GONE);
            textViewDescription.setVisibility(View.GONE);
        } else {
            textViewDescription.setText(Employee.get("description").toString().replace("\"" , ""));
        }

        //Ændrer tekstfelterne textViewLejeperiodeStart og Slut afhængig af brugerens status
        if (Employee.get("status").toString().replaceAll("\"", "").equals("ikke udlejet")){
            textViewLejeperiodeStart.setText("Ikke udlejet");
            textViewLejeperiodeSlut.setText("Ikke udlejet");
        } else if (Employee.get("status").toString().replace("\"", "").equals("sat til udleje")){
            textViewLejeperiodeStart.setText("Sat til udleje");
            textViewLejeperiodeSlut.setText("Sat til udleje");
        } else {
            textViewLejeperiodeStart.setText(Employee.get("startDate").toString().replaceAll("\"",""));
            textViewLejeperiodeSlut.setText(Employee.get("endDate").toString().replaceAll("\"",""));
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
            Intent intent = new Intent(this.getContext(), rentOutEmployee.class);
            intent.putExtra("entryString", entry.getValue().toString());
            startActivity(intent);
            //getActivity().finish();

        });

            //System.out.println(src);
            URL url = null;
            try {
                url = new URL(Employee.get("pic").toString().replace("\"", ""));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }



            if(Employee.get("pic").toString().replace("\"", "").equals("flexicu")){
                Glide.with(this)
                        .load(R.drawable.flexiculogocube)
                        .apply(RequestOptions.circleCropTransform())
                        .into(profilePic);
            } else {
                //We want to download images for the list of workers
                URL finalUrl = url;
                Glide.with(this)
                        .load(finalUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(profilePic);
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
        imageButtonArrow.setOnClickListener((test) -> {
        expand(linearLayoutExpanded, imageButtonArrow);
        });

        //Tilføjer Cardviewet og Spaceren til det Linære Layout myContainer.
        if(Employee.get("status").toString().replaceAll("\"", "").equals("ikke udlejet")){
            myContainer.addView(ExpandableCardview,0);
            myContainer.addView(Spacer,0);
        } else {
            myContainer.addView(ExpandableCardview,myContainer.getChildCount());
            myContainer.addView(Spacer,myContainer.getChildCount());
            allredyRentOut = true;
        }


        existingViews.add(Employee.get("ID").toString().replaceAll("\"",""));
        }

    private void expand(LinearLayout linearLayoutExpanded, ImageView imageButtonArrow) {
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
