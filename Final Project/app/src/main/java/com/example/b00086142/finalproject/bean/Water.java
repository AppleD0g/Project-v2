package com.example.b00086142.finalproject.bean;

public class Water {
    private String time;
    private int hasDrink;

    public Water() {
    }

    public Water(String time, int hasDrink) {
        this.time = time;
        this.hasDrink = hasDrink;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHasDrink() {
        return hasDrink;
    }

    public void setHasDrink(int hasDrink) {
        this.hasDrink = hasDrink;
    }

    @Override
    public String toString() {
        return time + " Drink:" + hasDrink +"ml";
    }
}
