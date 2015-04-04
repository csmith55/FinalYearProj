package com.example.csmith.myapplication.backend;

/**
 * Created by csmith on 01/04/15.
 */
public class LocationDetails {

    private final String id;
    private final double lat;
    private final double lng;

    public LocationDetails(String id, double lat, double lng) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
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
}
