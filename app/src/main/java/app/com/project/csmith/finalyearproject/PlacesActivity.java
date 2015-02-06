package app.com.project.csmith.finalyearproject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by csmith on 06/02/15.
 */
public class PlacesActivity extends Activity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlacesFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class PlacesFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_places, container, false);

            double usersLng;
            double usersLat;
            double friendsLng = 0;
            double friendsLat = 0;

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("LNG") && intent.hasExtra("LAT")) {
                usersLng = intent.getDoubleExtra("UserLng", 0);
                usersLat = intent.getDoubleExtra("UserLat", 0);
                friendsLng = intent.getDoubleExtra("LNG", 0);
                friendsLat = intent.getDoubleExtra("LAT", 0);

                Location location = getLocation();

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                if(location!=null) {
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

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



                GoogleMap map = getMapFragment().getMap();

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(friendsLat, friendsLng)).zoom(14).build(); // Creates a CameraPosition from the builder
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(friendsLat, friendsLng)));
                marker.showInfoWindow();

                FetchPlaces fetchPlaces = new FetchPlaces();
                fetchPlaces.execute(map);
            }

            return rootView;
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

        public Location getLocation() {
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    getLocation();

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            locationManager.requestLocationUpdates(locationProvider,0,0,locationListener);

            final Location lastKnown = locationManager.getLastKnownLocation(locationProvider);

            locationManager.removeUpdates(locationListener);
            return lastKnown;
        }
    }
}
