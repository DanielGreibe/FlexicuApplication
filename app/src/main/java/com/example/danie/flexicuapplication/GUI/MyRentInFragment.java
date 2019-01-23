package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.LogicLayer.RoundedImageView;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class MyRentInFragment extends Fragment {

    int id = 1;
    ArrayList<String> existingViews = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_rent_in, container, false);
        LinearLayout myContainer = view.findViewById(R.id.scrollViewLayout2);
        ScrollView myScrollView = view.findViewById(R.id.scrollviewUdlej);
        TextView title = view.findViewById(R.id.textView4);
        //Load workers from database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefIndlejninger = database.getReference("Users/"+GlobalVariables.getFirebaseUser().getUid()+"/Indlejninger");
        //Load employees and create cardviews and add to scroller
        myRefIndlejninger.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            //Hent liste over udlejede medarbejdere
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot entry : snapshot.getChildren()) {
                    createNewEmployee(entry, myContainer);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error!");
            }
        });

        myRefIndlejninger.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
        });
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    public void createNewEmployee(DataSnapshot entry, LinearLayout myContainer)
    {
        //Inflater to XML filer ind, et Cardview og en Spacer som bruges til at skabe afstand fordi det ikke er muligt med Padding eller Layout Margin.
        View ExpandableCardview = getLayoutInflater().inflate(R.layout.employee_cardview, null, false);
        View Spacer = getLayoutInflater().inflate(R.layout.spacer, null, false);

        //Lav FindViewById på Viewsne som er blevet inflated
        TextView textViewPay = ExpandableCardview.findViewById(R.id.textViewLøn);
        TextView textViewZipcode = ExpandableCardview.findViewById(R.id.textViewZipcode);
        TextView textViewDistance = ExpandableCardview.findViewById(R.id.textViewDistance);
        TextView textViewStatus = ExpandableCardview.findViewById(R.id.textViewHeaderStatus);
        LinearLayout linearLayoutCollapsed = ExpandableCardview.findViewById(R.id.linearLayoutCollapsed);
        LinearLayout linearLayoutExpanded = ExpandableCardview.findViewById(R.id.linearLayoutExpanded);
        ImageView imageButtonArrow = ExpandableCardview.findViewById(R.id.imageButtonExpand);
        TextView textViewName = ExpandableCardview.findViewById(R.id.textViewName);
        TextView textViewProfession = ExpandableCardview.findViewById(R.id.textViewProfession);
        ImageView profilePic = ExpandableCardview.findViewById(R.id.imageViewImage);
        Button b = ExpandableCardview.findViewById(R.id.buttonUdlej);
        b.setText("Bedøm");
        TextView textViewDescription = ExpandableCardview.findViewById(R.id.textViewDescription);
        TextView headerDescription = ExpandableCardview.findViewById(R.id.textViewHeaderDescription);

        b.setOnClickListener((test) ->
        {
            View popupView = getLayoutInflater().inflate(R.layout.popup_window, null);
            Button buttonRate = popupView.findViewById(R.id.buttonRate);
            RatingBar ratingBar = popupView.findViewById(R.id.ratingBar);



            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


            popupWindow.showAtLocation(textViewPay, Gravity.CENTER, 0, 0);

            buttonRate.setOnClickListener((Rating) -> {
                Float rating = ratingBar.getRating();

                //Closes the popup window
                popupWindow.dismiss();
            });


        });

        //Hent data og gør det til et JsonObject
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(entry.getValue().toString());
        JsonObject Employee = element.getAsJsonObject();

        if(existingViews.contains(Employee.get("ID").toString().replaceAll("\"", ""))){
            return;
        }else{
            existingViews.add(Employee.get("ID").toString().replaceAll("\"", ""));
        }

        //Træk data ud af Json Objektet og put det på textviews i Cardviewet.
        textViewPay.setText(Employee.get("pay").toString().replaceAll("\"",""));
        textViewName.setText(Employee.get("name").toString().replace("\"" , ""));
        textViewProfession.setText(Employee.get("job").toString().replace("\"" , ""));

        if(Employee.get("description").toString().replace("\"" , "").equals("")){
            headerDescription.setVisibility(View.GONE);
            textViewDescription.setVisibility(View.GONE);
        } else {
            textViewDescription.setText(Employee.get("description").toString().replace("\"" , ""));
        }

        //Set temporary picture while real pictures are downloading
        profilePic.setImageResource(R.drawable.download);
        //We want to download images for the list of workers
        if(Employee.get("pic").toString().replace("\"", "").equals("flexicu")){
//        if(false){
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

            URL url = null;
            try {
                url = new URL(Employee.get("pic").toString().replace("\"", ""));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //We want to download images for the list of workers
            URL finalUrl = url;
            Glide.with(this)
                    .load(finalUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profilePic);
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