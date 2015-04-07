package app.com.project.csmith.finalyearproject.PlaceDetails;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import app.com.project.csmith.finalyearproject.Places.PlacesActivity;
import app.com.project.csmith.finalyearproject.R;

/**
 * Created by csmith on 09/02/15.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private static final String hashTagShare = " #Friends";

    private String name;
    private LatLng latLng;
    private String text[] = new String[1];
    private int PROXIMITY_RADIUS = 500;
    private String GOOGLE_API_KEY = "AIzaSyCvXb5QrKw5BkVIVTxC1BMe5xr_KuFaDMQ";
    private Intent intent;
    private String location;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        intent = getActivity().getIntent();
        if (checkIntentExtras()) {
            assignVariables();
            setLocationAndCheckinInfo(rootView);
            setProfilePic(rootView);
            createGetInTouchButton(rootView);
            createMapAndMarker();
        }

        return rootView;
    }

    private void assignVariables() {
        name = intent.getStringExtra("FBNAMES");
        location = intent.getStringExtra("Location");
        latLng = intent.getParcelableExtra("latLng");
    }

    private void createMapAndMarker() {
        GoogleMap map = getMapFragment().getMap();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).bearing(90).tilt(30).build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(name).snippet(location));
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
                .setText(name);
    }

    private boolean checkIntentExtras() {
        return intent != null && intent.hasExtra("FBNAMES") && intent.hasExtra("FBPICS");
    }

    private void applyIntents() {
        Intent placesIntent = new Intent(getActivity(), PlacesActivity.class)
                .putExtra("latLng", latLng);
        startActivity(placesIntent);
    }

    private MapFragment getMapFragment() {
        android.app.FragmentManager fm;

        Log.d("SDK", "sdk: " + Build.VERSION.SDK_INT);
        Log.d("RELEASE", "release: " + Build.VERSION.RELEASE);

        fm = checkSDKVersionForFM();

        return (MapFragment) fm.findFragmentById(R.id.map);
    }

    private FragmentManager checkSDKVersionForFM() {
        FragmentManager fm;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.d("GEO", "using getFragmentManager");
            fm = getFragmentManager();
        } else {
            Log.d("GEO", "using getChildFragmentManager");
            fm = getChildFragmentManager();
        }
        return fm;
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

}

