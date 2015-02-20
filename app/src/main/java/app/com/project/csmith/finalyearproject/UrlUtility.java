package app.com.project.csmith.finalyearproject;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by csmith on 10/02/15.
 */
public  class UrlUtility {
    private static final String LOG_TAG = "";



    public static String makeConnection(String placeId){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

            Uri builtUri = buildUri(placeId);

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

    private static Uri buildUri(String placeId) {

        if(placeId != null && !placeId.isEmpty()){
            return buildDetailsUri(placeId);
        }
        else {
            return buildPlacesUri();
        }


    }

    private static Uri buildPlacesUri() {
        String longLat = "54.564169, -6.0012803";
        final String PLACES_URL =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        final String KEY = "AIzaSyAyA9rBQZyqzPdK8Te2NtyP-qboFaPzrC8";
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

    private static Uri buildDetailsUri(String placeId) {
        final String detailsUrl = "https://maps.googleapis.com/maps/api/place/details/json?";
        final String key = "AIzaSyAyA9rBQZyqzPdK8Te2NtyP-qboFaPzrC8";

        return Uri.parse(detailsUrl).buildUpon().appendQueryParameter("placeid",placeId).appendQueryParameter("key",key).build();
    }

    private static HttpURLConnection getHttpURLConnection(Uri builtUri) throws IOException {
        URL url = new URL(builtUri.toString());

        Log.v(LOG_TAG, "Built URI " + builtUri.toString());

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        return urlConnection;
    }
}
