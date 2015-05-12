package app.com.project.csmith.finalyearproject.PlacesUI;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import app.com.project.csmith.finalyearproject.R;

public class DetailFragment extends Fragment {

    private String name;
    private LatLng latLng;
    private Intent intent;
    private String location;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Inflates the view and obtains the intent extras passed from DetailActivity
     */
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

    /**
     * Extracts the variables from the intent
     */
    private void assignVariables() {
        name = intent.getStringExtra("FBNAMES");
        location = intent.getStringExtra("Location");
        latLng = intent.getParcelableExtra("latLng");
    }

    /**
     * Creates map and sets the position to show using friend's latlag.
     * Adds the information to the map marker extracted from intents
     */
    private void createMapAndMarker() {
        GoogleMap map = getMapFragment().getMap();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).bearing(90).tilt(30).build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(name).snippet(location));
        marker.showInfoWindow();
    }

    /**
     * Creates the "Go!" button
     * Overrides onClick to apply the intents to be passed to next fragment
     * @param rootView view to add button to
     */
    private void createGetInTouchButton(View rootView) {
        Button button = (Button) rootView.findViewById(R.id.getInTouch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyIntents();
            }
        });
    }

    /**
     * Sets the profile pictures of the friend
     * @param rootView adds to the view
     */
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

    /**
     * Sets the intents to pass to next fragment
     */
    private void applyIntents() {
        Intent placesIntent = new Intent(getActivity(), PlacesActivity.class)
                .putExtra("latLng", latLng);
        startActivity(placesIntent);
    }

    /**
     * Checks the current SDK Fragment manager on the phone
     * Phone's handle maps differently depending on SDK
     * @return map fragment
     */
    private MapFragment getMapFragment() {
        android.app.FragmentManager fm;
        fm = checkSDKVersionForFM();

        return (MapFragment) fm.findFragmentById(R.id.map);
    }

    /**
     * Checks the SDK version
     * @return fragment manager
     */
    private FragmentManager checkSDKVersionForFM() {
        FragmentManager fm;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            fm = getFragmentManager();
        } else {
            fm = getChildFragmentManager();
        }
        return fm;
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}

