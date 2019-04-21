package com.example.b00086142.finalproject.bean;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Walk implements Serializable {
    private String time;
    private double distance;
    private ArrayList<LatLng> arrOfLatLng;
    private long duration;

    private double speed;
    public Walk() {
    }

    public Walk(String time, double distance, ArrayList<LatLng> arrOfLatLng, long duration) {
        this.time = time;
        this.distance = distance;
        this.arrOfLatLng = arrOfLatLng;
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ArrayList<LatLng> getArrOfLatLng() {
        ArrayList<LatLng> lls = new ArrayList<>();
        for (Object o : arrOfLatLng) {
            if (o instanceof Map) {
                Map<String, Double> m = (Map<String, Double>) o;
                Double latitude = m.get("latitude");
                Double longitude = m.get("longitude");
                lls.add(new LatLng(latitude, longitude));
            }
        }
        return lls.size() > 0?lls:arrOfLatLng;
    }

    public void setArrOfLatLng(ArrayList<LatLng> arrOfLatLng) {
        this.arrOfLatLng = arrOfLatLng;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return time +"  Walk:"+ distance+" m";
    }
}
