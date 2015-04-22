package com.example.csmith.myapplication.backend;


/**
 * The object model for the data we are sending through endpoints
 */
public class MyBean {

    private LocationDetails myData;

    public LocationDetails getData() {
        return myData;
    }

    public void setData(LocationDetails data) {
        myData = data;
    }
}