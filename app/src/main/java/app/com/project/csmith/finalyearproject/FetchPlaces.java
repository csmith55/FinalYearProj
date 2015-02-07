package app.com.project.csmith.finalyearproject;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchPlaces extends AsyncTask<GoogleMap, Void, Places[]> {

    private final String LOG_TAG = FetchPlaces.class.getSimpleName();
    private GoogleMap googleMap;
    private ArrayList<Marker> entertainmentMarkers = new ArrayList<>();
    private ArrayList<Marker> foodDrinkMarkers = new ArrayList<>();
    private ArrayList<Marker> shoppingMarkers = new ArrayList<>();
    private ArrayList<Marker> healthBeautyMarkers = new ArrayList<>();
    private ArrayList<Marker> servicesMarkers = new ArrayList<>();
    private ArrayList<Marker> otherMarkers = new ArrayList<>();
    String[] foodDrink = {"cafe","bar","meal_delivery","meal_takeaway","restaurant", "food","bakery"};
    String[] entertainment = {"amusement_park","bowling_alley","casino","gym","movie_rental","movie_theater","night_club","stadium","aquarium","zoo"};
    String[] shopping = {"bicycle_store","book_store", "clothing_store", "convenience_store", "department_store", "electronics_store", "florist", "furniture_store", "grocery_or_supermarket", "hardware_store", "home_goods_store", "jewelry_store", "liquor_store", "pet_store", "shopping_mall", "shoe_store", "store"};
    String[] healthBeauty = {"beauty_salon", "dentist", "doctor", "hair_care", "health", "hospital", "pharmacy", "physiotherapist", "spa", "veterinary_care"};
    String[] services = {"atm", "bank", "bus_station", "car_rental", "car_dealer" ,"car_repair", "car_wash", "electrician", "fire_station", "gas_station", "laundry", "library", "lodging", "plumber", "police", "real_estate_agency", "subway_station", "taxi_stand", "train_station", "travel_agency"};



    private Places[] getPlacesFromJson(String placesJson)
            throws JSONException {

//        android.os.Debug.waitForDebugger();


        final String RESULTS = "results";
        final String GEOMETRY = "geometry";
        final String NAME = "name";
        final String ICON = "icon";
        final String TYPES = "types";
        final String OPENING_HOURS = "opening_hours";

        JSONObject places = new JSONObject(placesJson);
        JSONArray placesArray = places.getJSONArray(RESULTS);

        Places[] pArray = new Places[placesArray.length()];

        for(int i = 0; i < placesArray.length(); i++) {

            JSONObject jsonPlace = placesArray.getJSONObject(i);
            double lat = jsonPlace.getJSONObject(GEOMETRY).getJSONObject("location").getDouble("lat");
            double lng = jsonPlace.getJSONObject(GEOMETRY).getJSONObject("location").getDouble("lng");
            String name = jsonPlace.getString(NAME);
            String type = jsonPlace.getString(TYPES);


            pArray[i] = new Places(lat,lng,name,type);


        }


        return pArray;

    }
    @Override
    protected Places[] doInBackground(GoogleMap... params) {
       // android.os.Debug.waitForDebugger();

        googleMap = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        String placesJsonString = null;



        try {

            String longLat = "54.564169, -6.0012803";

            final String PLACES_URL =
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
            final String KEY = "AIzaSyCvXb5QrKw5BkVIVTxC1BMe5xr_KuFaDMQ";
            final String LOCATION = longLat;
            final String RADIUS = "500";


            final String KEY_PARAM = "key";
            final String LOCATION_PARAM="location";
            final String RADIUS_PARAM="radius";

            Uri builtUri = Uri.parse(PLACES_URL).buildUpon()
                    .appendQueryParameter(LOCATION_PARAM, LOCATION)
                    .appendQueryParameter(RADIUS_PARAM, RADIUS)
                    .appendQueryParameter(KEY_PARAM, KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {

                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {

                return null;
            }
            placesJsonString = buffer.toString();


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

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
        for(int i = 0; i < places.length; i++){
            Places place = places[i];

            if(isFoodDrink(place, foodDrink)){

                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(),place.getLng()))
                        .title(place.getName())
                        .snippet(place.getType()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).visible(false));
               foodDrinkMarkers.add(marker);

            }

            else if(isEntertainment(place, entertainment)){
                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(),place.getLng()))
                        .title(place.getName())
                        .snippet(place.getType()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).visible(false));
                entertainmentMarkers.add(marker);

            }

            else if(isShopping(place, shopping)){
                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(),place.getLng()))
                        .title(place.getName())
                        .snippet(place.getType()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).visible(false));
                shoppingMarkers.add(marker);

            }

            else if(isHealthBeauty(place, healthBeauty)){
                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(),place.getLng()))
                        .title(place.getName())
                        .snippet(place.getType()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).visible(false));
                healthBeautyMarkers.add(marker);
            }

            else if(isService(place, services)){
                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(),place.getLng()))
                        .title(place.getName())
                        .snippet(place.getType()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).visible(false));
                servicesMarkers.add(marker);
            }
            else {
                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(),place.getLng()))
                        .title(place.getName())
                        .snippet(place.getType()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).visible(false));
                otherMarkers.add(marker);
            }




        }
    }



    private boolean isService(Places place, String[] services) {
        for (String s : services){
            if (place.getType().contains(s)){
                return true;
            }
        }
        return false;
    }

    private boolean isHealthBeauty(Places place, String[] healthBeauty) {
        for(String s : healthBeauty){
            if (place.getType().contains(s)){
                return true;
            }
        }
        return false;
    }

    private boolean isShopping(Places place, String[] shopping) {
        for (String s : shopping){
            if(place.getType().contains(s)){
                return true;
            }
        }
        return false;
    }

    private boolean isEntertainment(Places place, String[] entertainment) {
        for(String s : entertainment){
            if(place.getType().contains(s)){
                return true;
            }
        }
        return false;
    }

    private boolean isFoodDrink(Places place, String[] foodDrink) {
        for(String s : foodDrink){
            if(place.getType().contains(s)){
              return true;
            }
        }
        return false;
    }


    public void loopServices() {
        for(Marker marker : getServicesMarkers()){
            marker.setVisible(!marker.isVisible());
        }
    }

    public void loopHealth() {
        for(Marker marker : getHealthBeautyMarkers()){
            marker.setVisible(!marker.isVisible());
        }
    }

    public void loopEntertainment() {
        for(Marker marker : getEntertainmentMarkers()){
            marker.setVisible(!marker.isVisible());
        }
    }

    public void loopShopping() {
        for(Marker marker : getShoppingMarkers()){
            marker.setVisible(!marker.isVisible());
        }
    }

    public void loopOthers() {
        for(Marker marker : getOtherMarkers()){
            marker.setVisible(!marker.isVisible());
        }
    }

    public void loopFoodDrink() {
        for(Marker marker : getFoodDrinkMarkers()){
            marker.setVisible(!marker.isVisible());
        }
    }

    public ArrayList<Marker> getEntertainmentMarkers() {
        return entertainmentMarkers;
    }

    public void setEntertainmentMarkers(ArrayList<Marker> entertainmentMarkers) {
        this.entertainmentMarkers = entertainmentMarkers;
    }

    public ArrayList<Marker> getFoodDrinkMarkers() {
        return foodDrinkMarkers;
    }

    public void setFoodDrinkMarkers(ArrayList<Marker> foodDrinkMarkers) {
        this.foodDrinkMarkers = foodDrinkMarkers;
    }

    public ArrayList<Marker> getShoppingMarkers() {
        return shoppingMarkers;
    }

    public void setShoppingMarkers(ArrayList<Marker> shoppingMarkers) {
        this.shoppingMarkers = shoppingMarkers;
    }

    public ArrayList<Marker> getHealthBeautyMarkers() {
        return healthBeautyMarkers;
    }

    public void setHealthBeautyMarkers(ArrayList<Marker> healthBeautyMarkers) {
        this.healthBeautyMarkers = healthBeautyMarkers;
    }

    public ArrayList<Marker> getServicesMarkers() {
        return servicesMarkers;
    }

    public void setServicesMarkers(ArrayList<Marker> servicesMarkers) {
        this.servicesMarkers = servicesMarkers;
    }

    public ArrayList<Marker> getOtherMarkers() {
        return otherMarkers;
    }

    public void setOtherMarkers(ArrayList<Marker> otherMarkers) {
        this.otherMarkers = otherMarkers;
    }
}
