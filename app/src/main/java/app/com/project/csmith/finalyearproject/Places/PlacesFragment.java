package app.com.project.csmith.finalyearproject.Places;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import app.com.project.csmith.finalyearproject.R;
import app.com.project.csmith.finalyearproject.Utilities.GeocoderUtil;

/**
 * Created by csmith on 09/02/15.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PlacesFragment extends Fragment implements View.OnClickListener {


    public static final String LAT_LNG = "latLng";
    List<Button> buttons = new ArrayList<>(7);
    Button blueButton, cyanButton, greenButton, orangeButton, magentaButton, violetButton, findOutMore;
    FetchPlaces fetchPlaces;

    boolean blueClicked, cyanClicked, greenClicked, orangeClicked, magentaClicked, violetClicked;
    Drawable drawable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_places, container, false);

        LatLng latLng;

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(LAT_LNG)) {
            latLng = intent.getParcelableExtra(LAT_LNG);

            displayAddressInfo(rootView, latLng);
            setupButtons(rootView);
            GoogleMap map = getMapFragment().getMap();
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                }
            });

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(14).build(); // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            Marker marker = map.addMarker(new MarkerOptions().position(latLng));
            marker.showInfoWindow();

            fetchPlaces.setLatLng(latLng);

            fetchPlaces.execute(map);
        }

        return rootView;
    }

    private void displayAddressInfo(View rootView, LatLng latLng) {
        List<Address> addresses = GeocoderUtil.convertLatLngToAddress(latLng, getActivity());
        ((TextView) rootView.findViewById(R.id.userLoc)).setText(addresses.get(0).getAddressLine(0) + "\n" + addresses.get(0).getAddressLine(1));
    }

    private void setupButtons(View rootView) {
        assignButtons(rootView);


        setClickListeners();
        fetchPlaces = new FetchPlaces(findOutMore);
    }

    private void setClickListeners() {
        blueButton.setOnClickListener(this);
        cyanButton.setOnClickListener(this);
        greenButton.setOnClickListener(this);
        orangeButton.setOnClickListener(this);
        magentaButton.setOnClickListener(this);
        violetButton.setOnClickListener(this);
        findOutMore.setOnClickListener(this);
    }


    private void assignButtons(View rootView) {
        blueButton = (Button) rootView.findViewById(R.id.blueMarker);
        cyanButton = (Button) rootView.findViewById(R.id.cyanMarker);
        greenButton = (Button) rootView.findViewById(R.id.greenMarker);
        orangeButton = (Button) rootView.findViewById(R.id.orangeMarker);
        magentaButton = (Button) rootView.findViewById(R.id.magentaMarker);
        violetButton = (Button) rootView.findViewById(R.id.violetMarker);
        findOutMore = (Button) rootView.findViewById(R.id.findOutMore);
        findOutMore.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        filterColours(view);

    }

    public void filterColours(View v) {
        switch (v.getId()) {
            case (R.id.blueMarker):
                filterBlueMarkers();
                break;
            case (R.id.cyanMarker):
                filterCyanMarkers();
                break;
            case (R.id.greenMarker):
                filterGreenMarkers();
                break;
            case (R.id.magentaMarker):
                filterMagentaMarkers();
                break;
            case (R.id.orangeMarker):
                filterOrangeMarkers();
                break;
            case (R.id.violetMarker):
                filterVioletMarkers();
                break;
            case (R.id.findOutMore):
                findOutMoreDetails();
        }

    }

    private void findOutMoreDetails() {
        Intent intent = new Intent(getActivity(), PlaceDetailActivity.class)
                .putExtra("Place", fetchPlaces.getCurrentPlace());
        startActivity(intent);
    }

    private void filterVioletMarkers() {
        violetClicked = !violetClicked;
        violetButton.setBackgroundColor(violetClicked ? Color.rgb(143, 0, 255) : Color.DKGRAY);
        fetchPlaces.loopFoodDrink();
    }

    private void filterOrangeMarkers() {
        orangeClicked = !orangeClicked;
        orangeButton.setBackgroundColor(orangeClicked ? Color.rgb(255, 127, 0) : Color.DKGRAY);
        fetchPlaces.loopOthers();
    }

    private void filterMagentaMarkers() {
        magentaClicked = !magentaClicked;
        magentaButton.setBackgroundColor(magentaClicked ? Color.rgb(255, 0, 255) : Color.DKGRAY);
        fetchPlaces.loopShopping();
    }

    private void filterGreenMarkers() {
        greenClicked = !greenClicked;
        greenButton.setBackgroundColor(greenClicked ? Color.rgb(0, 255, 0) : Color.DKGRAY);
        fetchPlaces.loopEntertainment();
    }

    private void filterCyanMarkers() {
        cyanClicked = !cyanClicked;
        cyanButton.setBackgroundColor(cyanClicked ? Color.rgb(0, 255, 255) : Color.DKGRAY);
        fetchPlaces.loopHealth();
    }

    private void filterBlueMarkers() {
        blueClicked = !blueClicked;
        blueButton.setBackgroundColor(blueClicked ? Color.rgb(0, 127, 255) : Color.DKGRAY);
        fetchPlaces.loopServices();
    }


    private MapFragment getMapFragment() {
        android.app.FragmentManager fm = null;

        Log.d("SDK", "sdk: " + Build.VERSION.SDK_INT);
        Log.d("RELEASE", "release: " + Build.VERSION.RELEASE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.d("GEO", "using getFragmentManager");
            fm = getFragmentManager();
        } else {
            Log.d("GEO", "using getChildFragmentManager");
            fm = getChildFragmentManager();
        }

        return (MapFragment) fm.findFragmentById(R.id.placesMap);
    }


}

