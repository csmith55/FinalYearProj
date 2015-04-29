package app.com.project.csmith.finalyearproject.Utilities;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by csmith on 10/02/15.
 */
public class UrlUtility {
    private static final String LOG_TAG = "";


    public static String makeConnection(String placeId, ArrayList<LatLng> latLng, boolean distanceApi) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            Uri builtUri;
            if (placeId != null) {
                builtUri = buildUri(placeId);
            } else if (distanceApi) {
                builtUri = buildUri(latLng);
            } else {
                builtUri = buildUri(latLng.get(0));
            }

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
            return builder.toString();


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


    }

    private static Uri buildUri(ArrayList<LatLng> latLng) {
        final String PLACES_URL =
                "https://maps.googleapis.com/maps/api/distancematrix/json?";
        final String KEY = "AIzaSyCvXb5QrKw5BkVIVTxC1BMe5xr_KuFaDMQ";
        final String ORIGIN = String.valueOf(latLng.get(0).latitude) + "," + String.valueOf(latLng.get(0).longitude);
        final String DESTINATION = String.valueOf(latLng.get(1).latitude) + "," + String.valueOf(latLng.get(1).longitude);


        final String KEY_PARAM = "key";
        final String ORIGIN_PARAM = "origins";
        final String DESTINATION_PARAM = "destinations";

        Log.d("Build URI", "Distance url");
        return Uri.parse(PLACES_URL).buildUpon()
                .appendQueryParameter(ORIGIN_PARAM, ORIGIN)
                .appendQueryParameter(DESTINATION_PARAM, DESTINATION)
                .appendQueryParameter(KEY_PARAM, KEY)
                .build();

    }


    private static Uri buildUri(LatLng latLng) {

        final String PLACES_URL =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        final String KEY = "AIzaSyCvXb5QrKw5BkVIVTxC1BMe5xr_KuFaDMQ";
        final String RADIUS = "500";


        final String KEY_PARAM = "key";
        final String LOCATION_PARAM = "location";
        final String RADIUS_PARAM = "radius";

        return Uri.parse(PLACES_URL).buildUpon()
                .appendQueryParameter(LOCATION_PARAM, String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude))
                .appendQueryParameter(RADIUS_PARAM, RADIUS)
                .appendQueryParameter(KEY_PARAM, KEY)
                .build();
    }

    private static Uri buildUri(String placeId) {
        final String detailsUrl = "https://maps.googleapis.com/maps/api/place/details/json?";
        final String key = "AIzaSyCvXb5QrKw5BkVIVTxC1BMe5xr_KuFaDMQ";

        return Uri.parse(detailsUrl).buildUpon().appendQueryParameter("placeid", placeId).appendQueryParameter("key", key).build();
    }

    private static HttpURLConnection getHttpURLConnection(Uri builtUri) throws IOException {
        URL url = new URL(builtUri.toString());

        Log.v(LOG_TAG, "Built URI " + builtUri.toString());

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        return urlConnection;
    }

    public static String makeConnection(String placeId) {
        return makeConnection(placeId, null, false);
    }

    public static String makeConnection(LatLng latLng) {
        ArrayList<LatLng> latLngs = new ArrayList<>();
        latLngs.add(latLng);
        return makeConnection(null, latLngs, false);
    }

    public static String makeConnection(LatLng usersLatLng, LatLng detailsLatLng) {
        ArrayList<LatLng> latLngs = new ArrayList<>();
        latLngs.add(usersLatLng);
        latLngs.add(detailsLatLng);
        return makeConnection(null, latLngs, true);
    }
}
