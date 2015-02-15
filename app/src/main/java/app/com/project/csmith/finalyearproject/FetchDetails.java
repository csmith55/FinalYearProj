package app.com.project.csmith.finalyearproject;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FetchDetails extends AsyncTask<Places,Void,Places> {
    private static final String LOG_TAG = "";
    private Places place;

    @Override
    protected Places doInBackground(Places... params) {
        place = params[0];


        String jsonDetailsString = UrlUtility.makeConnection(place.getPlaceId());
        try {
            return getDetailsFromJson(jsonDetailsString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }


    private Places getDetailsFromJson(String jsonDetailsString) throws JSONException {

        JSONObject placeDetails = new JSONObject(jsonDetailsString);

        double rating = 0;
        if(placeDetails.getJSONObject("result").has("rating")) {
             rating = placeDetails.getJSONObject("result").getDouble("rating");
        }
        String website = null;
        if(placeDetails.getJSONObject("result").has("website")) {
             website = placeDetails.getJSONObject("result").getString("website");
        }
        String icon = null;
        if(placeDetails.getJSONObject("result").has("icon")) {
             icon = placeDetails.getJSONObject("result").getString("icon");
        }
        String phoneNumber = null;
        if(placeDetails.getJSONObject("result").has("international_phone_number")) {
             phoneNumber = placeDetails.getJSONObject("result").getString("international_phone_number");
        }
        Reviews[] reviews = new Reviews[0];
        if(placeDetails.getJSONObject("result").has("reviews")) {
            JSONArray reviewsArray = placeDetails.getJSONObject("result").getJSONArray("reviews");
             reviews = new Reviews[reviewsArray.length()];

            for (int i = 0; i < reviewsArray.length(); i++) {

                JSONObject jsonReview = reviewsArray.getJSONObject(i);
                String author = jsonReview.getString("author_name");
                double authorRating = jsonReview.getDouble("rating");
                String authorText = jsonReview.getString("text");

                reviews[i] = new Reviews(author, authorRating, authorText);

            }
        }

        place.setReviewRating(rating);
        place.setWebsite(website);
        place.setImage(icon);
        place.setPhoneNumber(phoneNumber);
        place.setReviews(reviews);
        return place;
    }



}
