package app.com.project.csmith.finalyearproject.PlacesUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import app.com.project.csmith.finalyearproject.PlacesTasks.FetchDetails;
import app.com.project.csmith.finalyearproject.Places.Places;
import app.com.project.csmith.finalyearproject.R;


public class PlaceDetailsFragment extends android.app.Fragment {


    private static final String LOG_TAG = "";
    public static final String PLACE = "Place";
    ListView listView;
    private View view;
    private Places place;

    public PlaceDetailsFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Inflates the view and obtains the intent extras passed
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_place_details, container, false);
        setView(rootView);


        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(PLACE)) {
            getPlaceDetails(intent);
            displayDetails(rootView);

        }
        return rootView;
    }

    /**
     * Displays the details extracted from the intent
     * @param rootView view to add details
     */
    private void displayDetails(View rootView) {
        ((TextView) rootView.findViewById(R.id.placeName)).setText(place.getName());
        ((TextView) rootView.findViewById(R.id.placeWebsite)).setText(place.getWebsite());

        ((TextView) rootView.findViewById(R.id.placeNumber)).setText(place.getPhoneNumber());
        displayRatingAndColour(rootView);
        new GetImageViaUrl((ImageView) rootView.findViewById(R.id.placeIcon)).execute(place.getImage());

        displayReviews(rootView);
    }

    /**
     * Displays the reviews from Places object
     * @param rootView view to add details
     */
    private void displayReviews(View rootView) {
        listView = (ListView) rootView.findViewById(R.id.placeReviews);

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < place.getReviews().length; i++) {
            strings.add(place.getReviews()[i].getAuthor() + "\n" + place.getReviews()[i].getRating() + "\n" + place.getReviews()[i].getText());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, strings);
        listView.setAdapter(adapter);
    }

    /**
     * Displays the rating from Places object
     * Also sets the colour depending on the rating
     * @param rootView view to add details
     */
    private void displayRatingAndColour(View rootView) {
        TextView textView = (TextView) rootView.findViewById(R.id.placeRating);
        textView.setText(String.valueOf(place.getReviewRating()));
        if (place.getReviewRating() < 3) textView.setTextColor(Color.RED);
        else if (place.getReviewRating() >= 4) textView.setTextColor(Color.GREEN);
        else textView.setTextColor(Color.rgb(255, 165, 0));
    }

    /**
     * calls fetchDetails to retrieve the place's details
     * @param intent intent
     */
    private void getPlaceDetails(Intent intent) {
        place = (Places) intent.getParcelableExtra(PLACE);
        FetchDetails fetchDetails = new FetchDetails();
        fetchDetails.execute(place);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the menu action bar
     * Creates the share intent needed to send messages via other applications
     * @param menu menu
     * @param menuInflater menuInflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.detailfragment, menu);


        MenuItem menuItem = menu.findItem(R.id.action_share);

        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) menuItem.getActionProvider();

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareIntent(place));
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }

        super.onCreateOptionsMenu(menu, menuInflater);

    }

    /**
     * Creates the share provider message text to send
     * @param place place
     * @return intent
     */
    private Intent createShareIntent(Places place) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey! Let's meet up here: " + place.getName() + "\nSee you soon! #FYP");
        return shareIntent;
    }

    @android.support.annotation.Nullable
    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    /**
     * Retrieves the icon image from the details JSON
     */
    private class GetImageViaUrl extends AsyncTask<String, Void, Bitmap> {
        ImageView image;

        public GetImageViaUrl(ImageView image) {
            this.image = image;
        }

        protected Bitmap doInBackground(String... url) {

            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url[0]).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            image.setImageBitmap(result);
        }
    }
}
