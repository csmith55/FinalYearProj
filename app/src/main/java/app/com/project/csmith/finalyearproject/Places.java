package app.com.project.csmith.finalyearproject;

/**
 * Created by csmith on 05/02/15.
 */
public class Places {
    private double lng;
    private double lat;
    private String name;
    private String type;


    public Places(double lat, double lng, String name, String type) {
        this.setLng(lng);
        this.setLat(lat);
        this.setName(name);
        this.setType(type);

    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
