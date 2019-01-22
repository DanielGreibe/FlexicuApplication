package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
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
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.LogicLayer.RoundedImageView;
import com.example.danie.flexicuapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.example.danie.flexicuapplication.LogicLayer.GlobalVariables.getFirebaseUser;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;


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
        ScrollView myScrollView = view.findViewById(R.id.scrollviewUdlej);
        TextView title = view.findViewById(R.id.textView4);
        onAttach(getContext());
        //Load workers from database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefUdlejninger = database.getReference("Users/"+GlobalVariables.getFirebaseUser().getUid()+"/Udlejninger");
        //Load employees and create cardviews and add to scroller
        myRefUdlejninger.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            //Hent liste over udlejede medarbejdere
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot entry : snapshot.getChildren()) {
                    String temp = entry.toString().replaceAll("\"", "");
                    temp = temp.substring(temp.length()-6, temp.length()-2);
                    System.out.println("DATA ID IS: " + temp);
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
        });


        ValueEventListener newData = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot entry : dataSnapshot.getChildren()){
                    String temp = entry.toString().replaceAll("\"", "");
                    temp = temp.substring(temp.length()-6, temp.length()-2);
                    System.out.println("DATA ID IS: " + temp);
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

        //If calling activity is rent out employee, add Value even listener
        try{
            if(callingActivity.equals("udlejActivity")){
                myRefUdlejninger.addValueEventListener(newData);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
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
        Button buttonRating = ExpandableCardview.findViewById(R.id.buttonUdlej);
        ImageView profilePic = ExpandableCardview.findViewById(R.id.imageViewImage);

        //Hent data og gør det til et JsonObject
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(entry.getValue().toString());
        JsonObject Employee = element.getAsJsonObject();

        //Tjek status
        if(!Employee.get("status").toString().replaceAll("\"", "").equals("udlejet")){
            buttonRating.setEnabled(false);
            buttonRating.setBackgroundColor(Color.GRAY);
        }

        if(existingViews.contains(Employee.get("ID").toString().replaceAll("\"", ""))){
            return;
        }else{
            existingViews.add(Employee.get("ID").toString().replaceAll("\"", ""));
        }

        //Træk data ud af Json Objektet og put det på textviews i Cardviewet.
        textViewPay.setText(Employee.get("pay").toString() + " kr/t");
        textViewZipcode.setText(Employee.get("zipcode").toString());
        textViewDistance.setText(Employee.get("dist").toString() + " km");
        textViewName.setText(Employee.get("name").toString().replace("\"" , ""));
        textViewProfession.setText(Employee.get("job").toString().replace("\"" , ""));
        buttonRating.setText("Bedøm lejer");
        //Set temporary picture while real pictures are downloading
        profilePic.setImageResource(R.drawable.download);


        //We want to download images for the list of workers
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
        }


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
        //Lav OnClickListener som håndterer at viewet bliver expanded og collapsed.
        linearLayoutCollapsed.setOnClickListener((test) ->
        {
            expand(linearLayoutExpanded, imageButtonArrow);
        });
        imageButtonArrow.setOnClickListener((test) ->
        {
            expand(linearLayoutExpanded, imageButtonArrow);
        });
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
