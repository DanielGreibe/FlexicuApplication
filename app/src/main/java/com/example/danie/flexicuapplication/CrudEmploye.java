package com.example.danie.flexicuapplication;

import java.util.ArrayList;
import java.util.List;

public class CrudEmploye {
    String name;
    String job;
    double rank;
    double pay;
    int dist;
    boolean open;
    int id;
    int pic;


    public CrudEmploye(String name, String job, double rank, double pay, int dist, int id, int pic) {
        this.name = name;
        this.job = job;
        this.rank = rank;
        this.pay = pay;
        this.dist = dist;
        this.open = false;
        this.id = id;
        this.pic = pic;
    }


    //Getter
    public String getName() {
        return name;
    }
    public String getJob() {
        return job;
    }
    public double getRank() {
        return rank;
    }
    public double getPay() {
        return pay;
    }
    public int getDist() {
        return dist;
    }
    public boolean getOpen() {
        return open;
    }
    public boolean isOpen() {
        return open;
    }
    public int getId() {
        return id;
    }
    public int getPic() {
        return pic;
    }

    //Setter
    public void setName(String name) {
        this.name = name;
    }
    public void setJob(String job) {
        this.job = job;
    }
    public void setRank(double rank) {
        this.rank = rank;
    }
    public void setPay(double pay) {
        this.pay = pay;
    }
    public void setDist(int dist) {
        this.dist = dist;
    }
    public void setOpen(boolean open) {
        this.open = open;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setPic(int pic) {
        this.pic = pic;
    }
}
