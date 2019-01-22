package com.example.danie.flexicuapplication.LogicLayer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class CrudRentIns {

    int ID;
    String name;
    String job;
    String rentStart;
    String rentEnd;
    String owner;
    String location;
    String pay;
    String rank;
    String pic;
    String status;

    public CrudRentIns(String name, String pay, String job, String rentStart, String rentEnd, String owner, String location, String rank, String pic, String status) {
        this.name = name;
        this.pay = pay;
        this.job = job;
        this.rentStart = rentStart;
        this.rentEnd = rentEnd;
        this.owner = owner;
        this.location = location;
        this.rank = rank;
        this.pic = pic;
        this.status = status;

        boolean[] unique = {false};
        while (!unique[0]) {
            int random = new Random().nextInt(9000) + 1000;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(GlobalVariables.getFirebaseUser().getUid() + "/Medarbejdere");

            //Check for existing ID.
            myRef.child(String.valueOf(random)).addValueEventListener(new ValueEventListener() {
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
            this.ID = random;
            unique[0] = true;
        }
    }
    public int getID() {
        return ID;
    }

    public String getStatus(){ return status;}

    public void setStatus(String status){ this.status = status;}
}
