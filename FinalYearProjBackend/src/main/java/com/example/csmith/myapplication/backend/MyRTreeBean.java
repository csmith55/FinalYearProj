package com.example.csmith.myapplication.backend;


/**
 * The object model for the data we are sending through endpoints
 */
public class MyRTreeBean {

    private LocationDetails myData = new LocationDetails();

    public LocationDetails getData() {
        return myData;
    }

    public void setData(LocationDetails data) {
        myData = data;
    }
}