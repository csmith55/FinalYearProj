package app.com.project.csmith.finalyearproject.PlaceDetails;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.com.project.csmith.finalyearproject.Places.Places;
import app.com.project.csmith.finalyearproject.Utilities.UrlUtility;


public class FetchDetails extends AsyncTask<Places, Void, Places> {
    private static final String LOG_TAG = "";
    private Places place;
    final String RESULT = "result";
    final String RATING = "rating";
    final String WEBSITE = "website";
    final String ICON = "icon";
    final String REVIEWS = "reviews";
    final String INTERNATIONAL_PHONE_NUMBER = "international_phone_number";
    final String AUTHOR_NAME = "author_name";
    final String TEXT = "text";

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
        double rating = 0;
        String website = null, icon = null, phoneNumber = null;
        Reviews[] reviews = new Reviews[0];

        JSONObject placeDetails = new JSONObject(jsonDetailsString);

        if (placeDetails.getJSONObject(RESULT).has(RATING)) {
            rating = placeDetails.getJSONObject(RESULT).getDouble(RATING);
        }


        if (placeDetails.getJSONObject(RESULT).has(WEBSITE)) {
            website = placeDetails.getJSONObject(RESULT).getString(WEBSITE);
        }


        if (placeDetails.getJSONObject(RESULT).has(ICON)) {
            icon = placeDetails.getJSONObject(RESULT).getString(ICON);
        }


        if (placeDetails.getJSONObject(RESULT).has(INTERNATIONAL_PHONE_NUMBER)) {
            phoneNumber = placeDetails.getJSONObject(RESULT).getString(INTERNATIONAL_PHONE_NUMBER);
        }


        if (placeDetails.getJSONObject(RESULT).has(REVIEWS)) {
            reviews = getReviews(placeDetails);
        }

        return setPlaceDetails(rating, website, icon, phoneNumber, reviews);
    }

    private Places setPlaceDetails(double rating, String website, String icon, String phoneNumber, Reviews[] reviews) {
        place.setReviewRating(rating);
        place.setWebsite(website);
        place.setImage(icon);
        place.setPhoneNumber(phoneNumber);
        place.setReviews(reviews);
        return place;
    }

    private Reviews[] getReviews(JSONObject placeDetails) throws JSONException {
        Reviews[] reviews;JSONArray reviewsArray = placeDetails.getJSONObject(RESULT).getJSONArray(REVIEWS);
        reviews = new Reviews[reviewsArray.length()];

        for (int i = 0; i < reviewsArray.length(); i++) {

            JSONObject jsonReview = reviewsArray.getJSONObject(i);

            String author = jsonReview.getString(AUTHOR_NAME);
            double authorRating = jsonReview.getDouble(RATING);

            String authorText = jsonReview.getString(TEXT);

            reviews[i] = new Reviews(author, authorRating, authorText);

        }
        return reviews;
    }


}
