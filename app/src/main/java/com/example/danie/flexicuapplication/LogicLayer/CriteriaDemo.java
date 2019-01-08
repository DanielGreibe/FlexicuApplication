package com.example.danie.flexicuapplication.LogicLayer;

import java.util.ArrayList;
import java.util.List;

public class CriteriaDemo {
    public void start() {
        List<CrudEmployee> people = new ArrayList<CrudEmployee>();
        people.add(new CrudEmployee.EmployeBuilder("john").pay(201).job("mur").dist(70).builder());
        //people.add(new CrudEmployee.EmployeBuilder("fut").pay(199).builder());
        people.add(new CrudEmployee.EmployeBuilder("bob").pay(202).job("elektro").dist(20).builder());
        people.add(new CrudEmployee.EmployeBuilder("julemanden").pay(150).job("væg").dist(10).builder());
        CriteriaInterface pay = new CriteriaPay();
        CriteriaInterface profession = new CriteriaProfession();
        CriteriaInterface distance = new CriteriaDistance();

        System.out.println("løn-------");
        printPeeps(pay.meetCriteria(people));
        System.out.println("job---------");
        printPeeps(profession.meetCriteria(people));
        System.out.println(("distance-----------------"));
        printPeeps(distance.meetCriteria(people));
    }


    public static void printPeeps(List<CrudEmployee> peeps){
        for(CrudEmployee employee : peeps){
            System.out.println(employee.getName() + " - " + employee.getPay() + " - " +  employee.getJob());
        }

    }



}
