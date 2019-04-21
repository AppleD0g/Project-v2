package com.example.b00086142.finalproject.bean;

public class HeartRate {
    private long time;
    private int rate;

    public HeartRate() {
    }

    public HeartRate(long time, int rate) {
        this.time = time;
        this.rate = rate;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
