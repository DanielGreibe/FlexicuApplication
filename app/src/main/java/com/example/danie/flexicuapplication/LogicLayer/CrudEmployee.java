package com.example.danie.flexicuapplication.LogicLayer;

public class CrudEmployee
{
    private final String name;
    private final String job;
    private final double rank;
    private final double pay;
    private final int dist;
    boolean open;
    private final int pic;

    private CrudEmployee(EmployeBuilder builder){
        this.name = builder.name;
        this.job = builder.job;
        this.pay = builder.pay;
        this.dist = builder.dist;
        this.open = false;
        this.pic = builder.pic;
        this.rank = builder.rank;
    }
    public static class EmployeBuilder{
        String name;
        String job;
        double rank;
        double pay;
        int dist;
        boolean open;
        int pic;

        public EmployeBuilder(String name){
            this.name = name;

        }

        public EmployeBuilder job(String job){
            this.job = job;
            return  this;
        }

        public EmployeBuilder rank(double rank){
            this.rank = rank;
            return  this;
        }

        public EmployeBuilder pay(double pay){
            this.pay = pay;
            return this;
        }

        public  EmployeBuilder dist(int dist){
            this.dist = dist;
            return this;
        }

        public EmployeBuilder pic(int pic){
            this.pic = pic;
            return this;
        }

        public CrudEmployee builder(){
            return new CrudEmployee(this);
        }

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

}


