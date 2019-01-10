package com.example.danie.flexicuapplication.LogicLayer;

import java.util.ArrayList;
import java.util.List;

public class CriteriaDistance implements CriteriaInterface{
    int dist;
    @Override
    public List<CrudEmployee> meetCriteria(List<CrudEmployee> persons) {
        List<CrudEmployee> distance = new ArrayList<CrudEmployee>();

        for(CrudEmployee employee : persons){
            if(employee.getDist() < 10){
                distance.add(employee);
            }
        }
        return distance;
    }

    public int getCriteriaDist(){
        return dist;
    }

    public void setCriteriaDist(int dist){
        this.dist = dist;
    }
}
