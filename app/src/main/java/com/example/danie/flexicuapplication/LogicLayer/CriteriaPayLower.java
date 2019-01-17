package com.example.danie.flexicuapplication.LogicLayer;



import java.util.ArrayList;
import java.util.List;

public class CriteriaPayLower implements CriteriaInterface {
    private double searchCriteria;
    public CriteriaPayLower(double searchCriteria)
    {
        this.searchCriteria = searchCriteria;
    }
    @Override
    public List<CrudEmployee> meetCriteria(List<CrudEmployee> employees) {

        List<CrudEmployee> payLower = new ArrayList<CrudEmployee>();

            for(CrudEmployee employee: employees) {
                if(employee.getPay() > searchCriteria){
                    payLower.add(employee);
                }
            }

        return payLower;
    }

}
