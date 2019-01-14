package com.example.danie.flexicuapplication.LogicLayer;



import java.util.ArrayList;
import java.util.List;

public class CriteriaPay implements CriteriaInterface {
    private int searchCriteria;
    public CriteriaPay(int searchCriteria)
    {
        this.searchCriteria = searchCriteria;
    }
    @Override
    public List<CrudEmployee> meetCriteria(List<CrudEmployee> employees) {

        List<CrudEmployee> payOver200 = new ArrayList<CrudEmployee>();

            for(CrudEmployee employee: employees) {
                if(employee.getPay() == searchCriteria){
                    payOver200.add(employee);
                }
            }

        return payOver200;
    }

}
