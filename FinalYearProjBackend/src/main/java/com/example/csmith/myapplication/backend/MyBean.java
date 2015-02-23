package com.example.csmith.myapplication.backend;


/**
 * The object model for the data we are sending through endpoints
 */
public class MyBean {

    private double[] myData;

    public double[] getData() {
        return myData;
    }

    public void setData(double[] data) {
        myData = data;
    }
}