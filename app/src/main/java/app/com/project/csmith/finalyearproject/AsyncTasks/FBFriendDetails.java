package app.com.project.csmith.finalyearproject.AsyncTasks;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by csmith on 04/03/15.
 */
public class FBFriendDetails {

    private LatLng latLng;
    private String id;
    private Double distance;
    private String name;
    private String distanceText;

    public FBFriendDetails(String id, String name, LatLng latLng) {
        this.id = id;
        this.name = name;
        this.latLng = latLng;
    }

    public FBFriendDetails(String id, LatLng latLng) {
        this.id = id;
        this.latLng = latLng;
    }


    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMetricDistance() {
        return distance/1000;
    }

    public void setMetricDistance(Double distance) {
        this.distance = distance;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    public Double getImperialDistance() {
        return (distance/1000)*0.621371192;
    }


    @Override
    public boolean equals(Object obj){
        final FBFriendDetails compareID = (FBFriendDetails) obj;
        if(getID().equals(compareID.getID())){
            return true;
        }
        return false;
    }
}
