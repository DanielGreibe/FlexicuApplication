package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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


public class MyRentOutsFragment extends Fragment {

    int id = 1;
    ArrayList<String> existingViews = new ArrayList<>();
    String callingActivity = null;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_rent_outs, container, false);

        //Get intent and parse values
        Intent intent = getActivity().getIntent();
        callingActivity = intent.getStringExtra("callingActivity");

        LinearLayout myContainer = view.findViewById(R.id.scrollViewLayout2);
        onAttach(getContext());

        //Set database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefUdlejninger = database.getReference("Users/"+GlobalVariables.getFirebaseUser().getUid()+"/Udlejninger");

        //Load employees and create cardviews and add to scroller
        myRefUdlejninger.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            //Get list of subleased coworkers ID's
            public void onDataChange(DataSnapshot snapshot) {
                //For each coworker
                for (DataSnapshot entry : snapshot.getChildren()) {
                    //Parse owner and ID of coworker
                    String temp = entry.toString().replaceAll("\"", "");
                    temp = temp.substring(temp.length()-6, temp.length()-2);
                    System.out.println("DATA ID IS: " + temp);

                    //Set reference to get employee
                    DatabaseReference myRefMedarbejder = database.getReference("Users/"+GlobalVariables.getFirebaseUser().getUid()+"/Medarbejdere/"+temp);

                    //Get employee data
                    myRefMedarbejder.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                                createNewEmployee(snapshot, myContainer);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("Error!");
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error!");
            }
        });


        //Set listener for data change and update UI accordingly
        ValueEventListener newData = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!String.valueOf(getActivity()).contains("TabbedRentOut")){
                    return;
                }
                for(DataSnapshot entry : dataSnapshot.getChildren()){
                    String temp = entry.toString().replaceAll("\"", "");
                    temp = temp.substring(temp.length()-6, temp.length()-2);
                    DatabaseReference myRefMedarbejder = database.getReference("Users/"+GlobalVariables.getFirebaseUser().getUid()+"/Medarbejdere/"+temp);

                    //Hent udlejet medarbejder data
                    myRefMedarbejder.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            createNewEmployee(snapshot, myContainer);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("Error!");
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error!");
            }
        };
        myRefUdlejninger.addValueEventListener(newData);

        return view;
        }

    @SuppressLint("StaticFieldLeak")
    public void createNewEmployee(DataSnapshot entry, LinearLayout myContainer)
    {
        //Inflater to XML filer ind, et Cardview og en Spacer som bruges til at skabe afstand fordi det ikke er muligt med Padding eller Layout Margin.
        View ExpandableCardview = getLayoutInflater().inflate(R.layout.employee_cardview, null, false);
        View Spacer = getLayoutInflater().inflate(R.layout.spacer, null, false);

        //Do FindViewById on the Views which have been inflated
        TextView textViewPay = ExpandableCardview.findViewById(R.id.textViewLøn);
        TextView textViewZipcode = ExpandableCardview.findViewById(R.id.textViewZipcode);
        TextView textViewDistance = ExpandableCardview.findViewById(R.id.textViewDistance);
        TextView textViewStatus = ExpandableCardview.findViewById(R.id.textViewStatus);
        LinearLayout linearLayoutCollapsed = ExpandableCardview.findViewById(R.id.linearLayoutCollapsed);
        LinearLayout linearLayoutExpanded = ExpandableCardview.findViewById(R.id.linearLayoutExpanded);
        ImageView imageButtonArrow = ExpandableCardview.findViewById(R.id.imageButtonExpand);
        TextView textViewName = ExpandableCardview.findViewById(R.id.textViewName);
        TextView textViewProfession = ExpandableCardview.findViewById(R.id.textViewProfession);
        Button buttonRating = ExpandableCardview.findViewById(R.id.buttonUdlej);
        ImageView profilePic = ExpandableCardview.findViewById(R.id.imageViewImage);
        TextView textViewDescription = ExpandableCardview.findViewById(R.id.textViewDescription);
        TextView headerDescription = ExpandableCardview.findViewById(R.id.textViewHeaderDescription);


        //Convert to JSON-object
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(entry.getValue().toString());
        JsonObject Employee = element.getAsJsonObject();

        //If employee is already listed, return
        if(existingViews.contains(Employee.get("ID").toString().replaceAll("\"", ""))){
            return;
        }else{
            existingViews.add(Employee.get("ID").toString().replaceAll("\"", ""));
        }

        //Set data in newly created cardView
        textViewPay.setText(Employee.get("pay").toString() + " kr/t");
        textViewZipcode.setText(Employee.get("zipcode").toString());
        textViewDistance.setText(Employee.get("dist").toString() + " km");
        textViewName.setText(Employee.get("name").toString().replaceAll("\"" , ""));
        textViewProfession.setText(Employee.get("job").toString().replaceAll("\"" , ""));
        String employeeStatus = Employee.get("status").toString().replaceAll("\"", "");
        textViewStatus.setText(employeeStatus);

        //If emloyee has a description, display it
        if(Employee.get("description").toString().replace("\"" , "").equals("")){
            headerDescription.setVisibility(View.GONE);
            textViewDescription.setVisibility(View.GONE);
        } else {
            textViewDescription.setText(Employee.get("description").toString().replace("\"" , ""));
        }

        //Hvis employee has been rented to someone, let user evaluate this company
        buttonRating.setText("Bedøm indlejer");
        if(employeeStatus.equals("udlejet")){
            buttonRating.setEnabled(true);
        }else{
            buttonRating.setEnabled(false);
            buttonRating.setBackgroundColor(Color.GRAY);
        }


        //Set temporary picture while real pictures are downloading
        profilePic.setImageResource(R.drawable.download);

        //If no custom image, set Flexicu logo, else download from Firebase Storage, with link stored under Employee.get("pic")
        if(Employee.get("pic").toString().replace("\"", "").equals("flexicu")){
            //Get picture, make round and display
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
            //Download image, make round and display
            URL finalUrl = null;
            try {
                finalUrl = new URL(Employee.get("pic").toString().replace("\"", ""));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //We want to download images for the list of workers
            Glide.with(this)
                    .load(finalUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profilePic);
        }


        //Set OnClickListener that handles expansion and collapse of view
        linearLayoutCollapsed.setOnClickListener((test) ->
        {
            expand(linearLayoutExpanded, imageButtonArrow);
        });
        imageButtonArrow.setOnClickListener((test) ->
        {
            expand(linearLayoutExpanded, imageButtonArrow);
        });

        //Setup rating XML (no backend support for this yet)
        buttonRating.setOnClickListener((test) ->
        {
            View popupView = getLayoutInflater().inflate(R.layout.popup_window, null);
            Button buttonRate = popupView.findViewById(R.id.buttonRate);
            RatingBar ratingBar = popupView.findViewById(R.id.ratingBar);

            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            popupWindow.showAtLocation(textViewPay, Gravity.CENTER, 0, 0);
            View containerOfPopup = (View) popupWindow.getContentView().getParent();
            WindowManager windowManager = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams layoutParameters = (WindowManager.LayoutParams) containerOfPopup.getLayoutParams();

            // add flag
            layoutParameters.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParameters.dimAmount = 0.8f;
            windowManager.updateViewLayout(containerOfPopup, layoutParameters);

            buttonRate.setOnClickListener((Rating) -> {
                Float rating = ratingBar.getRating();

                //The value that that was entered in the ratingbar in the popupwindow
                Float average = Employee.get("rank").getAsFloat();

                //Takes the datasnapshot from the user and puts it into a string.
                String currentUser = entry.toString();

                //Splits the values of the employee based on ,
                String[] userData = currentUser.split(",");

                //Returns the value of rank of the user in the database.
                String currentRating = userData[9].split(":")[1];

                //Closes the popup window
                popupWindow.dismiss();
            });


        });

        //Add cardview and spacer to myContainer
        myContainer.addView(ExpandableCardview);
        myContainer.addView(Spacer);
    }

    //Expand / collapse function
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
