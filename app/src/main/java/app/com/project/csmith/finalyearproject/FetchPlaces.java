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

class FetchPlaces extends AsyncTask<GoogleMap, Void, Places[]> {

    private final String LOG_TAG = FetchPlaces.class.getSimpleName();
    private final String[] foodDrink, entertainment, shopping, healthBeauty, services;
    private GoogleMap googleMap;
    private ArrayList<Marker> entertainmentMarkers, foodDrinkMarkers, shoppingMarkers, healthBeautyMarkers, servicesMarkers;
    private ArrayList<Marker> otherMarkers = new ArrayList<>();

    public FetchPlaces() {
        entertainmentMarkers = new ArrayList<>();
        foodDrinkMarkers = new ArrayList<>();
        shoppingMarkers = new ArrayList<>();
        healthBeautyMarkers = new ArrayList<>();
        servicesMarkers = new ArrayList<>();
        otherMarkers = new ArrayList<>();

        foodDrink = new String[]{"cafe", "bar", "meal_delivery", "meal_takeaway", "restaurant", "food", "bakery"};
        entertainment = new String[]{"amusement_park", "bowling_alley", "casino", "gym", "movie_rental", "movie_theater", "night_club", "stadium", "aquarium", "zoo"};
        shopping = new String[]{"bicycle_store", "book_store", "clothing_store", "convenience_store", "department_store", "electronics_store", "florist", "furniture_store", "grocery_or_supermarket", "hardware_store", "home_goods_store", "jewelry_store", "liquor_store", "pet_store", "shopping_mall", "shoe_store", "store"};
        healthBeauty = new String[]{"beauty_salon", "dentist", "doctor", "hair_care", "health", "hospital", "pharmacy", "physiotherapist", "spa", "veterinary_care"};
        services = new String[]{"atm", "bank", "bus_station", "car_rental", "car_dealer", "car_repair", "car_wash", "electrician", "fire_station", "gas_station", "laundry", "library", "lodging", "plumber", "police", "real_estate_agency", "subway_station", "taxi_stand", "train_station", "travel_agency"};
    }


    private Places[] getPlacesFromJson(String placesJson)
            throws JSONException {

//        android.os.Debug.waitForDebugger();


        final String RESULTS = "results";
        final String GEOMETRY = "geometry";
        final String NAME = "name";
        final String TYPES = "types";

        JSONObject places = new JSONObject(placesJson);
        JSONArray placesArray = places.getJSONArray(RESULTS);

        Places[] pArray = new Places[placesArray.length()];

        getJsonLocationData(GEOMETRY, NAME, TYPES, placesArray, pArray);


        return pArray;

    }

    private void getJsonLocationData(String GEOMETRY, String NAME, String TYPES, JSONArray placesArray, Places[] pArray) throws JSONException {
        for (int i = 0; i < placesArray.length(); i++) {

            JSONObject jsonPlace = placesArray.getJSONObject(i);
            double lat = jsonPlace.getJSONObject(GEOMETRY).getJSONObject("location").getDouble("lat");
            double lng = jsonPlace.getJSONObject(GEOMETRY).getJSONObject("location").getDouble("lng");
            String name = jsonPlace.getString(NAME);
            String type = jsonPlace.getString(TYPES);


            pArray[i] = new Places(lat, lng, name, type);


        }
    }

    @Override
    protected Places[] doInBackground(GoogleMap... params) {
        // android.os.Debug.waitForDebugger();

        googleMap = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        String placesJsonString = null;


        try {

            Uri builtUri = buildUri();

            urlConnection = getHttpURLConnection(builtUri);

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            if (inputStream == null) {

                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {

                return null;
            }
            placesJsonString = builder.toString();


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
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

    private HttpURLConnection getHttpURLConnection(Uri builtUri) throws IOException {
        URL url = new URL(builtUri.toString());

        Log.v(LOG_TAG, "Built URI " + builtUri.toString());


        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        return urlConnection;
    }

    private Uri buildUri() {
        String longLat = "54.564169, -6.0012803";

        final String PLACES_URL =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        final String KEY = "AIzaSyCvXb5QrKw5BkVIVTxC1BMe5xr_KuFaDMQ";
        final String RADIUS = "500";


        final String KEY_PARAM = "key";
        final String LOCATION_PARAM = "location";
        final String RADIUS_PARAM = "radius";

        return Uri.parse(PLACES_URL).buildUpon()
                .appendQueryParameter(LOCATION_PARAM, longLat)
                .appendQueryParameter(RADIUS_PARAM, RADIUS)
                .appendQueryParameter(KEY_PARAM, KEY)
                .build();
    }


    @Override
    protected void onPostExecute(Places[] places) {
        Marker marker;

        for (Places place : places) {

            if (isFoodDrink(place, foodDrink)) {
                marker = addMarkerToMap(place, BitmapDescriptorFactory.HUE_VIOLET);
                foodDrinkMarkers.add(marker);

            } else if (isEntertainment(place, entertainment)) {
                marker = addMarkerToMap(place, BitmapDescriptorFactory.HUE_GREEN);
                entertainmentMarkers.add(marker);
            } else if (isShopping(place, shopping)) {
                marker = addMarkerToMap(place, BitmapDescriptorFactory.HUE_MAGENTA);
                shoppingMarkers.add(marker);

            } else if (isHealthBeauty(place, healthBeauty)) {
                marker = addMarkerToMap(place, BitmapDescriptorFactory.HUE_CYAN);
                healthBeautyMarkers.add(marker);
            } else if (isService(place, services)) {
                marker = addMarkerToMap(place, BitmapDescriptorFactory.HUE_AZURE);
                servicesMarkers.add(marker);
            } else {
                marker = addMarkerToMap(place, BitmapDescriptorFactory.HUE_ORANGE);
                otherMarkers.add(marker);
            }


        }
    }


    private Marker addMarkerToMap(Places place, float colour) {
        return googleMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(), place.getLng()))
                .title(place.getName())
                .snippet(place.getType()).icon(BitmapDescriptorFactory.defaultMarker(colour)).visible(false));

    }


    private boolean isService(Places place, String[] services) {
        for (String s : services) {
            if (place.getType().contains(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean isHealthBeauty(Places place, String[] healthBeauty) {
        for (String s : healthBeauty) {
            if (place.getType().contains(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean isShopping(Places place, String[] shopping) {
        for (String s : shopping) {
            if (place.getType().contains(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEntertainment(Places place, String[] entertainment) {
        for (String s : entertainment) {
            if (place.getType().contains(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean isFoodDrink(Places place, String[] foodDrink) {
        for (String s : foodDrink) {
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
}
