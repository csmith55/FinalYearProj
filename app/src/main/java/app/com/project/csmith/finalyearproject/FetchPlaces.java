package app.com.project.csmith.finalyearproject;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
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

public class FetchPlaces extends AsyncTask<GoogleMap, Void, Places[]> {

    private final String LOG_TAG = FetchPlaces.class.getSimpleName();
    private GoogleMap googleMap;


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

            String longLat = "54.5609420,-6.0013630";

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
        for(Places place:places){
            Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(),place.getLng()))
                    .title(place.getName())
                    .snippet(place.getType()));
        }
    }
}
