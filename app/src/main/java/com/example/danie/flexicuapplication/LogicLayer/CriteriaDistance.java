package com.example.danie.flexicuapplication.LogicLayer;

import java.util.ArrayList;
import java.util.List;

public class CriteriaDistance implements CriteriaInterface{
    private double searchCriteria;

    public CriteriaDistance(double searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override

    public List<CrudEmployee> meetCriteria(List<CrudEmployee> persons) {
        List<CrudEmployee> distance = new ArrayList<CrudEmployee>();

        for(CrudEmployee employee : persons){
            if(employee.getDist() < searchCriteria ){
                distance.add(employee);
            }
        }
        return distance;
    }

}
