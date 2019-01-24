package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.content.Context;
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


public class MyRentInFragment extends Fragment {

    int id = 1;
    ArrayList<String> existingViews = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Get Views
        View view = inflater.inflate(R.layout.fragment_my_rent_in, container, false);
        LinearLayout myContainer = view.findViewById(R.id.scrollViewLayout2);

        //Load workers from database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefIndlejninger = database.getReference("Users/"+GlobalVariables.getFirebaseUser().getUid()+"/Indlejninger");

        //Load employees and create cardviews and add to scroller
        myRefIndlejninger.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            //Get list of leased employees
            public void onDataChange(DataSnapshot snapshot) {
                //For each employee
                for (DataSnapshot entry : snapshot.getChildren()) {
                    //Create employee
                    createNewEmployee(entry, myContainer);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error!");
            }
        });

        //Add listener for changes to this list, and update UI accordingly
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

        //Do FindViewById on the Views which have been inflated
        TextView textViewPay = ExpandableCardview.findViewById(R.id.textViewLøn);
        LinearLayout linearLayoutCollapsed = ExpandableCardview.findViewById(R.id.linearLayoutCollapsed);
        LinearLayout linearLayoutExpanded = ExpandableCardview.findViewById(R.id.linearLayoutExpanded);
        ImageView imageButtonArrow = ExpandableCardview.findViewById(R.id.imageButtonExpand);
        TextView textViewName = ExpandableCardview.findViewById(R.id.textViewName);
        TextView textViewProfession = ExpandableCardview.findViewById(R.id.textViewProfession);
        ImageView profilePic = ExpandableCardview.findViewById(R.id.imageViewImage);
        Button b = ExpandableCardview.findViewById(R.id.buttonUdlej);
        TextView textViewDescription = ExpandableCardview.findViewById(R.id.textViewDescription);
        TextView headerDescription = ExpandableCardview.findViewById(R.id.textViewHeaderDescription);
        b.setText("Bedøm udlejer");

        //Setup rating XML (no backend support for this yet)
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
            View containerOfPopup = (View) popupWindow.getContentView().getParent();
            WindowManager windowManager = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams layoutParameters = (WindowManager.LayoutParams) containerOfPopup.getLayoutParams();
            // add flag
            layoutParameters.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParameters.dimAmount = 0.8f;
            windowManager.updateViewLayout(containerOfPopup, layoutParameters);

            buttonRate.setOnClickListener((Rating) -> {
                Float rating = ratingBar.getRating();

                //Closes the popup window
                popupWindow.dismiss();
            });
        });

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
        textViewPay.setText(Employee.get("pay").toString().replaceAll("\"",""));
        textViewName.setText(Employee.get("name").toString().replace("\"" , ""));
        textViewProfession.setText(Employee.get("job").toString().replace("\"" , ""));

        //If emloyee has a description, display it
        if(Employee.get("description").toString().replace("\"" , "").equals("")){
            headerDescription.setVisibility(View.GONE);
            textViewDescription.setVisibility(View.GONE);
        } else {
            textViewDescription.setText(Employee.get("description").toString().replace("\"" , ""));
        }

        //Set temporary picture while real pictures are downloading
        profilePic.setImageResource(R.drawable.download);

        //If no custom image, set Flexicu logo, else download from Firebase Storage, with link stored under Employee.get("pic")
        if(Employee.get("pic").toString().replace("\"", "").equals("flexicu")){
            Glide.with(this)
                    .load(R.drawable.flexiculogocube)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profilePic);
        } else {
            URL finalUrl = null;
            try {
                finalUrl = new URL(Employee.get("pic").toString().replace("\"", ""));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //Download image, make round and display
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