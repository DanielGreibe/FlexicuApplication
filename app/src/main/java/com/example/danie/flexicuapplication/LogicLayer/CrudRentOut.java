package com.example.danie.flexicuapplication.LogicLayer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Random;

public class CrudRentOut {
    int rentId;
    String id;
    String name;
    String job;
    String pic;
    String rentStart;
    String rentEnd;
    String rank;
    String pay;
    boolean onMarket;
    boolean taken;

    public CrudRentOut(String id, String name, String job, String pic, String rentStart, String rentEnd, String rank, String pay){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.id = id;
        this.name = name;
        this.job = job;
        this.pic = pic;
        this.rentStart = rentStart;
        this.rentEnd = rentEnd;
        this.rank = rank;
        this.pay = pay;
        this.onMarket = true;
        this.taken = false;


        boolean[] unique = {false};
        while (!unique[0]) {
            int random = new Random().nextInt(9000) + 1000;
           DatabaseReference myRefMedarbejder = database.getReference(GlobalVariables.getFirebaseUser().getUid() + "/Medarbejdere");

            //Check for existing ID.
            myRefMedarbejder.child(String.valueOf(random)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        System.out.println("Unique ID");
                        unique[0] = true;
                    } else {
                        System.out.println("ID not Unique");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Error!");
                    unique[0] = false;
                }
            });
            this.rentId = random;
            unique[0] = true;
        }
    }
    public int getRentId() {
        return rentId;
    }
}
