package com.example.b00086142.finalproject.bean;

import java.io.Serializable;

public class Sport implements Serializable {
    private String sname;//sport's name
    private String introduce;//sport's introduce

    private long traintime;//train's time
    private int sportRes;

    public Sport() {
    }

    public Sport(String sname) {
        this.sname = sname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public long getTraintime() {
        return traintime;
    }

    public void setTraintime(long traintime) {
        this.traintime = traintime;
    }

    public int getSportRes() {
        return sportRes;
    }

    public void setSportRes(int sportRes) {
        this.sportRes = sportRes;
    }
}
