package com.example.csmith.myapplication.backend;


import java.util.ArrayList;

/**
 * The object model for the data we are sending through endpoints
 */
public class MyRTreeBean {

    private ArrayList<LocationDetails> myData = new ArrayList<>();

    public ArrayList<LocationDetails> getData() {
        return myData;
    }

    public void setData(LocationDetails data) {
        myData.add(data);
    }
}