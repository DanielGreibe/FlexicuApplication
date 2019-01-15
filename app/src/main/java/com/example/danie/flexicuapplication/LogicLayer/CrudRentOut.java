package com.example.danie.flexicuapplication.LogicLayer;

public class CrudRentOut {
    String id;
    String name;
    String job;
    String pic;
    String rentStart;
    String rentEnd;
    double rent;
    double pay;

    public CrudRentOut(String id, String name, String job, String pic, String rentStart, String rentEnd, double rent, double pay){
        this.id = id;
        this.name = name;
        this.job = job;
        this.pic = pic;
        this.rentStart = rentStart;
        this.rentEnd = rentEnd;
        this.rent = rent;
        this.pay = pay;
    }
}
