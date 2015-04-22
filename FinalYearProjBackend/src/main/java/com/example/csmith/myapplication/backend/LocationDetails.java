package com.example.csmith.myapplication.backend;


import java.util.Date;

/**
 * Created by csmith on 01/04/15.
 */
public class LocationDetails {

    private final String id;
    private final double lat;
    private final double lng;
    private final Date date;

    public LocationDetails(String id, double lat, double lng, Date date) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.date = date;
    }

    public LocationDetails() {
        id = null;
        lat = 0;
        lng = 0;
        date = null;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }
}
