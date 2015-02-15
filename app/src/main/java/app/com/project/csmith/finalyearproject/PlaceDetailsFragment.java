package app.com.project.csmith.finalyearproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


public class PlaceDetailsFragment extends android.app.Fragment {


    private static final String LOG_TAG = "";
    private View view;
    private Places place;
    ListView listView;

    public PlaceDetailsFragment(){
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_place_details, container, false);
        setView(rootView);


        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("Place")) {

            place = (Places) intent.getParcelableExtra("Place");
            FetchDetails fetchDetails = new FetchDetails();
            fetchDetails.execute(place);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ((TextView) rootView.findViewById(R.id.placeName)).setText(place.getName());
            ((TextView) rootView.findViewById(R.id.placeWebsite)).setText(place.getWebsite());

            ((TextView) rootView.findViewById(R.id.placeNumber)).setText(place.getPhoneNumber());
            ((TextView) rootView.findViewById(R.id.placeRating)).setText(String.valueOf(place.getReviewRating()));
            new GetImageViaUrl((ImageView) rootView.findViewById(R.id.placeIcon)).execute(place.getImage());

             listView = (ListView) rootView.findViewById(R.id.placeReviews);

            ArrayList<String> strings = new ArrayList<>();
            for(int i = 0; i < place.getReviews().length;i++){
                strings.add(place.getReviews()[i].getAuthor() + "\n" + place.getReviews()[i].getRating() + "\n" + place.getReviews()[i].getText());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,strings);
            listView.setAdapter(adapter);







        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
       menuInflater.inflate(R.menu.detailfragment,menu);



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

    private Intent createShareIntent(Places place) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey! Let's meet up here: " + place.getName() + "\nSee you soon! #GeoSocial");
        return shareIntent;
    }

    public void setView(View view) {
        this.view = view;
    }

    @android.support.annotation.Nullable
    @Override
    public View getView() {
        return view;
    }


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
