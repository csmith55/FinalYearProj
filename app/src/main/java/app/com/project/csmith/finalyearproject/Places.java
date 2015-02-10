package app.com.project.csmith.finalyearproject;

/**
 * Created by csmith on 05/02/15.
 */
public class Places {
    private double lng;
    private double lat;
    private String name;
    private String type;
    private String placeId;
    private String website;
    private String phoneNumber;
    private double reviewRating;
    private String image;
    private Reviews[] reviews;



    public Places(double lat, double lng, String name, String type, String placeId) {
        this.setLng(lng);
        this.setLat(lat);
        this.setName(name);
        this.setType(type);
        this.setPlaceId(placeId);

    }

    public double getLng() {
        return lng;
    }

    private void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    private void setLat(double lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(double reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Reviews[] getReviews() {
        return reviews;
    }

    public void setReviews(Reviews[] reviews) {
        this.reviews = reviews;
    }
}
