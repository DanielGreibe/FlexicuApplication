package com.example.danie.flexicuapplication.LogicLayer;

import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.Random;

public class CrudEmployee
    {
    private final String name;
    private final String job;
    private final double rank;
    private final double pay;
    private final int dist;
    boolean open;
    private String pic;
    private final int ID;
    private final int zipcode;
    private boolean available;


    private CrudEmployee(EmployeBuilder builder)
        {
        this.name = builder.name;
        this.job = builder.job;
        this.pay = builder.pay;
        this.dist = builder.dist;
        this.open = false;
        this.pic = builder.pic;
        this.rank = builder.rank;
        this.ID = builder.ID;
        this.zipcode = builder.zipcode;
        this.available = builder.available;
        }

    public static class EmployeBuilder
        {
        String name;
        String job;
        double rank;
        double pay;
        int dist;
        boolean open;
        String pic;
        int ID;
        int zipcode;
        boolean available;

        public EmployeBuilder(String name)
            {
            this.name = name;
            //Add ID here

            final boolean[] unique = {false};
            while (!unique[0])
                {
                int random = new Random().nextInt(9000) + 1000;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(GlobalVariables.getFirebaseUser().getUid() + "/Medarbejdere");

                //Check for existing ID.
                myRef.child(String.valueOf(random)).addValueEventListener(new ValueEventListener()
                    {
                    @Override
                    public void onDataChange(DataSnapshot snapshot)
                        {
                        if (!snapshot.exists())
                            {
                            System.out.println("Unique ID");
                            unique[0] = true;
                            }
                        else
                            {
                            System.out.println("ID not Unique");
                            }
                        }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                        {
                        System.out.println("Error!");
                        unique[0] = false;
                        }
                    });


                this.ID = random;
                unique[0] = true;

                }
            }

        public EmployeBuilder available(boolean available)
            {
            this.available = available;
            return this;
            }

        public EmployeBuilder job(String job)
            {
            this.job = job;
            return this;
            }

        public EmployeBuilder rank(double rank)
            {
            this.rank = rank;
            return this;
            }

        public EmployeBuilder pay(double pay)
            {
            this.pay = pay;
            return this;
            }

        public EmployeBuilder dist(int dist)
            {
            this.dist = dist;
            return this;
            }

        public EmployeBuilder pic(String pic)
            {
            this.pic = pic;
            return this;
            }

        public EmployeBuilder ID(int ID)
            {
            this.ID = ID;
            return this;
            }

        public EmployeBuilder zipcode(int zipcode)
            {
            this.zipcode = zipcode;
            return this;
            }

        public CrudEmployee builder()
            {
            return new CrudEmployee(this);
            }

        public void setName(String name)
            {
            this.name = name;
            }

        public void setJob(String job)
            {
            this.job = job;
            }

        public void setRank(double rank)
            {
            this.rank = rank;
            }

        public void setPay(double pay)
            {
            this.pay = pay;
            }

        public void setDist(int dist)
            {
            this.dist = dist;
            }

        public void setOpen(boolean open)
            {
            this.open = open;
            }

        public void setPic(String pic)
            {
            this.pic = pic;
            }

        public void setavailable(boolean available) {this.available = available;}
        }

    //Getter
    public String getName()
        {
        return name;
        }

    public String getJob()
        {
        return job;
        }

    public double getRank()
        {
        return rank;
        }

    public double getPay()
        {
        return pay;
        }

    public int getDist()
        {
        return dist;
        }

    public String getPic()
        {
        return pic;
        }

    public int getID()
        {
        return ID;
        }

    public void setPic(String URL)
        {
        pic = URL;
        }

    public boolean getAvailable()
        {
        return available;
        }

        public int getZipcode(){return zipcode;}


    //Setter

    }


