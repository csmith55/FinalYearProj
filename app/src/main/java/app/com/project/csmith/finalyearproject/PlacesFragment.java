package app.com.project.csmith.finalyearproject;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by csmith on 09/02/15.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PlacesFragment extends Fragment implements View.OnClickListener {

    FetchPlaces fetchPlaces = new FetchPlaces();
    Button blueButton, cyanButton, greenButton, orangeButton, magentaButton, violetButton, findOutMore;

    boolean blueClicked, cyanClicked, greenClicked, orangeClicked, magentaClicked, violetClicked;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_places, container, false);

        LatLng latLng;

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("latLng")) {
            latLng = intent.getParcelableExtra("latLng");



            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());
            if (latLng != null) {
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getAddressLine(1);
                    String country = addresses.get(0).getAddressLine(2);
                    Log.d("Location Address : ", address);
                    Log.d("Location City : ", city);
                    Log.d("Location country : ", country);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ((TextView) rootView.findViewById(R.id.userLoc)).setText(addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + addresses.get(0).getAddressLine(2));
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

    private void setupButtons(View rootView) {
        blueButton = (Button) rootView.findViewById(R.id.blueMarker);
        cyanButton = (Button) rootView.findViewById(R.id.cyanMarker);
        greenButton = (Button) rootView.findViewById(R.id.greenMarker);
        orangeButton = (Button) rootView.findViewById(R.id.orangeMarker);
        magentaButton = (Button) rootView.findViewById(R.id.magentaMarker);
        violetButton = (Button) rootView.findViewById(R.id.violetMarker);
        findOutMore = (Button) rootView.findViewById(R.id.findOutMore);



        blueButton.setOnClickListener(this);
        cyanButton.setOnClickListener(this);
        greenButton.setOnClickListener(this);
        orangeButton.setOnClickListener(this);
        magentaButton.setOnClickListener(this);
        violetButton.setOnClickListener(this);
        findOutMore.setOnClickListener(this);
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
        violetButton.setBackgroundColor(violetClicked ? Color.rgb(238, 130, 238) : Color.BLACK);
        fetchPlaces.loopFoodDrink();
    }

    private void filterOrangeMarkers() {
        orangeClicked = !orangeClicked;
        orangeButton.setBackgroundColor(orangeClicked ? Color.rgb(255, 165, 0) : Color.BLACK);
        fetchPlaces.loopOthers();
    }

    private void filterMagentaMarkers() {
        magentaClicked = !magentaClicked;
        magentaButton.setBackgroundColor(magentaClicked ? Color.MAGENTA : Color.BLACK);
        fetchPlaces.loopShopping();
    }

    private void filterGreenMarkers() {
        greenClicked = !greenClicked;
        greenButton.setBackgroundColor(greenClicked ? Color.GREEN : Color.BLACK);
        fetchPlaces.loopEntertainment();
    }

    private void filterCyanMarkers() {
        cyanClicked = !cyanClicked;
        cyanButton.setBackgroundColor(cyanClicked ? Color.CYAN : Color.BLACK);
        fetchPlaces.loopHealth();
    }

    private void filterBlueMarkers() {
        blueClicked = !blueClicked;
        blueButton.setBackgroundColor(blueClicked ? Color.BLUE : Color.BLACK);
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

