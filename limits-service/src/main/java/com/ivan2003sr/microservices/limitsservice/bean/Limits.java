package com.ivan2003sr.microservices.limitsservice.bean;

public class Limits {
    private int minimun;
    private int maximun;

    public Limits(){
        super();

    }
    public Limits(int minimun, int maximun) {
        this.minimun = minimun;
        this.maximun = maximun;
    }



    public int getMinimun() {
        return minimun;
    }

    public void setMinimun(int minimun) {
        this.minimun = minimun;
    }

    public int getMaximun() {
        return maximun;
    }

    public void setMaximun(int maximun) {
        this.maximun = maximun;
    }


}
