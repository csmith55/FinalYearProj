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

        double rating = placeDetails.getDouble("rating");
        String website = placeDetails.getString("website");
        String icon = placeDetails.getString("icon");
        String phoneNumber = placeDetails.getString("international_phone_number");

        JSONArray reviewsArray = placeDetails.getJSONArray("reviews");
        Reviews[] reviews = new Reviews[reviewsArray.length()];

        for (int i = 0; i < reviewsArray.length(); i++) {

            JSONObject jsonReview = reviewsArray.getJSONObject(i);
            String author = jsonReview.getString("author_name");
            double authorRating = jsonReview.getDouble("rating");
            String authorText = jsonReview.getString("text");

           reviews[i] = new Reviews(author,authorRating,authorText);

        }

        place.setReviewRating(rating);
        place.setWebsite(website);
        place.setImage(icon);
        place.setPhoneNumber(phoneNumber);
        place.setReviews(reviews);
        return place;
    }
}
