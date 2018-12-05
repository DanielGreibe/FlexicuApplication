package com.example.danie.flexicuapplication;

import java.util.ArrayList;
import java.util.List;

public class CrudEmployee
    {
    String name;
    String job;
    double rank;
    double pay;
    int dist;
    boolean open;
    int pic;
    List<CrudEmployee> personer = new ArrayList<CrudEmployee>();


    public CrudEmployee(String name, String job, double pay, int dist, int pic){
        this.name = name;
        this.job = job;
        this.pay = pay;
        this.dist = dist;
        this.open = false;
        this.pic = pic;
        this.rank = 0;
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

    public void setPic(int pic) {
        this.pic = pic;
    }
}
