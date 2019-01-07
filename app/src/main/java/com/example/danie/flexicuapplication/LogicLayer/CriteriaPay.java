package com.example.danie.flexicuapplication.LogicLayer;



import java.util.ArrayList;
import java.util.List;

public class CriteriaPay implements CriteriaInterface {
    @Override
    public List<CrudEmployee> meetCriteria(List<CrudEmployee> employees) {
        List<CrudEmployee> payOver200 = new ArrayList<CrudEmployee>();

            for(CrudEmployee employee: employees) {
                if(employee.getPay() > 200){
                    payOver200.add(employee);
                }
            }

        return payOver200;
    }
}
