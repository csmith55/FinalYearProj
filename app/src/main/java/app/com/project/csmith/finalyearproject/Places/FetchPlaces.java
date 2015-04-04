package app.com.project.csmith.finalyearproject.Places;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.com.project.csmith.finalyearproject.Utilities.UrlUtility;

class FetchPlaces extends AsyncTask<GoogleMap, Void, Places[]> {

    public static final String LOCATION = "location";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String PLACE_ID = "place_id";
    private final String LOG_TAG = FetchPlaces.class.getSimpleName();
    private String[] foodDrink, entertainment, shopping, healthBeauty, services;
    private LatLng latLng;
    private GoogleMap googleMap;
    private ArrayList<Marker> otherMarkers, entertainmentMarkers, foodDrinkMarkers, shoppingMarkers, healthBeautyMarkers, servicesMarkers;
    private Places[] pArray;
    private Places currentPlace;
    private Button findOutMore;
    final String RESULTS = "results";
    final String GEOMETRY = "geometry";
    final String NAME = "name";
    final String TYPES = "types";

    public FetchPlaces(Button findOutMore) {
        this.findOutMore = findOutMore;

        initialiseMarkers();

        assignMarkersContent();
    }

    private void assignMarkersContent() {
        foodDrink = new String[]{"cafe", "bar", "meal_delivery", "meal_takeaway", "restaurant", "food", "bakery"};
        entertainment = new String[]{"amusement_park", "bowling_alley", "casino", "gym", "movie_rental", "movie_theater",
                "night_club", "stadium", "aquarium", "zoo"};
        shopping = new String[]{"bicycle_store", "book_store", "clothing_store", "convenience_store", "department_store",
                "electronics_store", "florist", "furniture_store", "grocery_or_supermarket", "hardware_store", "home_goods_store",
                "jewelry_store", "liquor_store", "pet_store", "shopping_mall", "shoe_store", "store"};
        healthBeauty = new String[]{"beauty_salon", "dentist", "doctor", "hair_care", "health", "hospital", "pharmacy",
                "physiotherapist", "spa", "veterinary_care"};
        services = new String[]{"atm", "bank", "bus_station", "car_rental", "car_dealer", "car_repair", "car_wash", "electrician",
                "fire_station", "gas_station", "laundry", "library", "lodging", "plumber", "police", "real_estate_agency",
                "subway_station", "taxi_stand", "train_station", "travel_agency"};
    }

    private void initialiseMarkers() {
        entertainmentMarkers = new ArrayList<>();
        foodDrinkMarkers = new ArrayList<>();
        shoppingMarkers = new ArrayList<>();
        healthBeautyMarkers = new ArrayList<>();
        servicesMarkers = new ArrayList<>();
        otherMarkers = new ArrayList<>();
    }


    private Places[] getPlacesFromJson(String placesJson)
            throws JSONException {

        JSONObject places = new JSONObject(placesJson);
        JSONArray placesArray = places.getJSONArray(RESULTS);

        pArray = new Places[placesArray.length()];

        getJsonLocationData(GEOMETRY, NAME, TYPES, placesArray, pArray);


        return pArray;

    }

    private void getJsonLocationData(String GEOMETRY, String NAME, String TYPES, JSONArray placesArray, Places[] pArray) throws JSONException {
        for (int i = 0; i < placesArray.length(); i++) {

            JSONObject jsonPlace = placesArray.getJSONObject(i);
            double lat = jsonPlace.getJSONObject(GEOMETRY).getJSONObject(LOCATION).getDouble(LAT);
            double lng = jsonPlace.getJSONObject(GEOMETRY).getJSONObject(LOCATION).getDouble(LNG);
            String name = jsonPlace.getString(NAME);
            String type = jsonPlace.getString(TYPES);
            String placeId = jsonPlace.getString(PLACE_ID);


            pArray[i] = new Places(lat, lng, name, type, placeId);

        }
    }


    @Override
    protected Places[] doInBackground(GoogleMap... params) {
        googleMap = params[0];

        String placesJsonString = UrlUtility.makeConnection(latLng);
        try {
            return getPlacesFromJson(placesJsonString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Places[] places) {

        for (Places place : places) {
            checkMarkerType(place);
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (pArray != null && marker.getTitle() != null) {
                    setCurrentPlaceVisibility(marker);
                }
                return false;
            }
        });
    }

    private void setCurrentPlaceVisibility(Marker marker) {
        for (Places place : pArray) {
            if (marker.getTitle().equals(place.getName())) {
                setCurrentPlace(place);
                findOutMore.setVisibility(View.VISIBLE);
            }
        }
    }

    private void checkMarkerType(Places place) {
        if (containsPlace(place, foodDrink)) {
            addFoodDrinkMarker(place);

        } else if (containsPlace(place, entertainment)) {
            addEntertainmentMarker(place);
        } else if (containsPlace(place, shopping)) {
            addShoppingMarker(place);

        } else if (containsPlace(place, healthBeauty)) {
            addHealthMarker(place);
        } else if (containsPlace(place, services)) {
            addServicesMarker(place);
        } else {
            addOthersMarker(place);
        }
    }

    private void addOthersMarker(Places place) {
        otherMarkers.add(addMarkerToMap(place, BitmapDescriptorFactory.HUE_ORANGE));
    }

    private void addServicesMarker(Places place) {
        servicesMarkers.add(addMarkerToMap(place, BitmapDescriptorFactory.HUE_AZURE));
    }

    private void addHealthMarker(Places place) {
        healthBeautyMarkers.add(addMarkerToMap(place, BitmapDescriptorFactory.HUE_CYAN));
    }

    private void addShoppingMarker(Places place) {
        shoppingMarkers.add(addMarkerToMap(place, BitmapDescriptorFactory.HUE_MAGENTA));
    }

    private void addEntertainmentMarker(Places place) {
        entertainmentMarkers.add(addMarkerToMap(place, BitmapDescriptorFactory.HUE_GREEN));
    }

    private void addFoodDrinkMarker(Places place) {
        foodDrinkMarkers.add(addMarkerToMap(place, BitmapDescriptorFactory.HUE_VIOLET));
    }


    private Marker addMarkerToMap(final Places place, float colour) {

        return googleMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(), place.getLng()))
                .title(place.getName())
                .snippet(place.getType()).icon(BitmapDescriptorFactory.defaultMarker(colour)).visible(false));

    }

    private boolean containsPlace(Places place, String[] listedTypes) {
        for (String s : listedTypes) {
            if (place.getType().contains(s)) {
                return true;
            }

        }
        return false;
    }


    public void loopServices() {
        for (Marker marker : getServicesMarkers()) {
            marker.setVisible(!marker.isVisible());
        }
    }

    public void loopHealth() {
        for (Marker marker : getHealthBeautyMarkers()) {
            marker.setVisible(!marker.isVisible());
        }
    }

    public void loopEntertainment() {
        for (Marker marker : getEntertainmentMarkers()) {
            marker.setVisible(!marker.isVisible());
        }
    }

    public void loopShopping() {
        for (Marker marker : getShoppingMarkers()) {
            marker.setVisible(!marker.isVisible());
        }
    }

    public void loopOthers() {
        for (Marker marker : getOtherMarkers()) {
            marker.setVisible(!marker.isVisible());
        }
    }

    public void loopFoodDrink() {
        for (Marker marker : getFoodDrinkMarkers()) {
            marker.setVisible(!marker.isVisible());
        }
    }

    private ArrayList<Marker> getEntertainmentMarkers() {
        return entertainmentMarkers;
    }

    public void setEntertainmentMarkers(ArrayList<Marker> entertainmentMarkers) {
        this.entertainmentMarkers = entertainmentMarkers;
    }

    private ArrayList<Marker> getFoodDrinkMarkers() {
        return foodDrinkMarkers;
    }

    public void setFoodDrinkMarkers(ArrayList<Marker> foodDrinkMarkers) {
        this.foodDrinkMarkers = foodDrinkMarkers;
    }

    private ArrayList<Marker> getShoppingMarkers() {
        return shoppingMarkers;
    }

    public void setShoppingMarkers(ArrayList<Marker> shoppingMarkers) {
        this.shoppingMarkers = shoppingMarkers;
    }

    private ArrayList<Marker> getHealthBeautyMarkers() {
        return healthBeautyMarkers;
    }

    public void setHealthBeautyMarkers(ArrayList<Marker> healthBeautyMarkers) {
        this.healthBeautyMarkers = healthBeautyMarkers;
    }

    private ArrayList<Marker> getServicesMarkers() {
        return servicesMarkers;
    }

    public void setServicesMarkers(ArrayList<Marker> servicesMarkers) {
        this.servicesMarkers = servicesMarkers;
    }

    private ArrayList<Marker> getOtherMarkers() {
        return otherMarkers;
    }

    public void setOtherMarkers(ArrayList<Marker> otherMarkers) {
        this.otherMarkers = otherMarkers;
    }

    public Places getCurrentPlace() {
        return currentPlace;
    }

    public void setCurrentPlace(Places currentPlace) {
        this.currentPlace = currentPlace;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
