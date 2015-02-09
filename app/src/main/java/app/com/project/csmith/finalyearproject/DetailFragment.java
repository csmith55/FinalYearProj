package app.com.project.csmith.finalyearproject;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by csmith on 09/02/15.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailFragment extends Fragment {

    private static final String LOG_TAG = app.com.project.csmith.finalyearproject.DetailFragment.class.getSimpleName();

    private static final String hashTagShare = " #Friends";
    private final double latitude = 54.564169;
    private final double longitude = -6.0012803;
    private String locationString;
    private String text[] = new String[1];
    private int PROXIMITY_RADIUS = 500;
    private String GOOGLE_API_KEY = "AIzaSyCvXb5QrKw5BkVIVTxC1BMe5xr_KuFaDMQ";
    private Intent intent;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        intent = getActivity().getIntent();
        if (checkIntentExtras()) {
            locationString = intent.getStringExtra("FBNAMES");
            text = locationString.split("Location:");
            setLocationAndCheckinInfo(rootView);

            setProfilePic(rootView);


            createGetInTouchButton(rootView);

            createMapAndMarker();


        }

        return rootView;
    }

    private void createMapAndMarker() {
        GoogleMap map = getMapFragment().getMap();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(14).bearing(90).tilt(30).build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(text[0]).snippet(text[1]));
        marker.showInfoWindow();
    }

    private void createGetInTouchButton(View rootView) {
        Button button = (Button) rootView.findViewById(R.id.getInTouch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyIntents();
            }
        });
    }

    private void setProfilePic(View rootView) {
        ProfilePictureView profilePictureView = (ProfilePictureView) rootView.findViewById(R.id.profile_pic);
        profilePictureView.setProfileId(intent.getStringExtra("FBPICS"));
        profilePictureView.setCropped(true);
    }

    private void setLocationAndCheckinInfo(View rootView) {
        ((TextView) rootView.findViewById(R.id.detail_text))
                .setText(text[0]);

        ((TextView) rootView.findViewById(R.id.checkins))
                .setText("12:00 - Checked in @ Queen's University\n" +
                        "10/12/2014 - Checked in @ Newcastle, Co.Down\n" +
                        "09/12/2014 - Checked in @ Movie House, Dublin Road\n" +
                        "08/12/2014 - Checked in @ Victoria Square Shopping Centre\n" +
                        "07/12/2014 - Checked in @ Belfast City Hall\n");
    }

    private boolean checkIntentExtras() {
        return intent != null && intent.hasExtra("FBNAMES") && intent.hasExtra("FBPICS");
    }

    private void applyIntents() {
        Intent placesIntent = new Intent(getActivity(), PlacesActivity.class)
                .putExtra("LNG", longitude);
        placesIntent.putExtra("LAT", latitude);
        placesIntent.putExtra("UserLng", intent.getDoubleExtra("UserLng", 0));
        placesIntent.putExtra("UserLat", intent.getDoubleExtra("UserLat", 0));

        startActivity(placesIntent);
    }

    private MapFragment getMapFragment() {
        android.app.FragmentManager fm;

        Log.d("SDK", "sdk: " + Build.VERSION.SDK_INT);
        Log.d("RELEASE", "release: " + Build.VERSION.RELEASE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.d("GEO", "using getFragmentManager");
            fm = getFragmentManager();
        } else {
            Log.d("GEO", "using getChildFragmentManager");
            fm = getChildFragmentManager();
        }

        return (MapFragment) fm.findFragmentById(R.id.map);
    }


    private void openPreferredLocationInMap() {

        String textSplit = text[1];
        String[] split = textSplit.split("Distance: ");
        String location = split[0];

        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(LOG_TAG, "Couldn't call " + location + ", no receiving apps installed!");
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);


        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) menuItem.getActionProvider();

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                locationString + hashTagShare);
        return shareIntent;
    }
}

