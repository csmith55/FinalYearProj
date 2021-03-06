package app.com.project.csmith.finalyearproject.HomeUI;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.com.project.csmith.finalyearproject.DistanceTasks.FBFriendDetails;
import app.com.project.csmith.finalyearproject.LocationTasks.GetLocationAsyncTask;
import app.com.project.csmith.finalyearproject.LocationTasks.UpdateLocationAsyncTask;
import app.com.project.csmith.finalyearproject.R;
import app.com.project.csmith.finalyearproject.PlacesUI.DetailActivity;
import app.com.project.csmith.finalyearproject.QueryUI.SettingsActivity;
import app.com.project.csmith.finalyearproject.Utilities.GeocoderUtil;


public class MainFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "MainFragment";
    private static final String FBPICS = "FBPICS";
    private static final String FBNAMES = "FBNAMES";
    private final Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(state);
        }
    };
    private ListView resultsList;
    private ArrayList<FBFriendDetails> friendDetails = new ArrayList<>();
    private UiLifecycleHelper uiHelper;
    private String usersFacebookId;
    private MainFragment mainFragment = this;
    CustomListAdapter customListAdapter;
    View view;

    private Intent intent;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            boolean switchPreference = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("ShareLocation",true);
            if (usersFacebookId != null && switchPreference ) {
                new UpdateLocationAsyncTask(usersFacebookId).execute(new Pair<Context, LatLng>(getActivity(), new LatLng(location.getLatitude(), location.getLongitude())));
                Log.d("UPDATELOC", "IN UPDATE LOC");

            }
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
    private LocationManager locationManager;
    private String locationProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
       setHasOptionsMenu(true);
        setLocationListener();
        updateFriends();
    }

    private void setLocationListener() {
        locationProvider = "";
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationProvider = LocationManager.GPS_PROVIDER;
        }
        else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }
        else {
            locationProvider = LocationManager.NETWORK_PROVIDER;
            Toast.makeText(getActivity(), "Please enable your location!", Toast.LENGTH_LONG).show();
        }


        locationManager.requestLocationUpdates(locationProvider, 30000, 10, locationListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void updateFriends() {
        if (Session.getActiveSession().isOpened()) {
            // Request user data and show the results
            Request.newMeRequest(Session.getActiveSession(), new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (null != user) {

                        Log.v(TAG, "Response : " + response);
                        Log.v(TAG, "UserID : " + user.getId());
                        Log.v(TAG, "User FirstName : " + user.getFirstName());
                        usersFacebookId = user.getId();

                    }
                }
            }).executeAsync();
        }
        makeMeRequest(Session.getActiveSession());
    }

    private void makeMeRequest(final Session activeSession) {
        Request request = Request.newMyFriendsRequest(activeSession,
                new Request.GraphUserListCallback() {
                    @Override
                    public void onCompleted(List<GraphUser> graphUsers, Response response) {
                        if (activeSession == Session.getActiveSession()) {
                            Location location = getLocation();
                            if (graphUsers != null && location != null) {
                                new GetLocationAsyncTask(graphUsers, friendDetails, mainFragment, new LatLng(location.getLatitude(), location.getLongitude())).execute();
                                Log.d("GETLOCS", "IN GET  LOC");
                            }
                        }

                    }
                });
        request.executeAsync();
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {



        view = inflater.inflate(R.layout.fragment_main, container, false);
        fbPermissionsLogin(view);

        // Find the user's profile picture custom view
        // Get a reference to the ListView, and attach this adapter to it.
        setAdapterAndIntents(view, new ArrayList<FBFriendDetails>());

        return view;

    }

    private void setAdapterAndIntents(View view,ArrayList<FBFriendDetails> details) {
        ArrayList<String> address = new ArrayList<>();

            for (int i = 0; i < details.size(); i++) {
                List<Address> addresses = GeocoderUtil.convertLatLngToAddress(details.get(i).getLatLng(), getActivity());
                address.add(!addresses.isEmpty() ? addresses.get(0).getAddressLine(0) : "");
            }
            customListAdapter = new CustomListAdapter(getActivity(), address, details);
            resultsList = (ListView) view.findViewById(R.id.list);
            resultsList.setAdapter(customListAdapter);
            resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    createDetailActivityIntent(position);
                }
            });
        }

    private void createDetailActivityIntent(int position) {
        String name = customListAdapter.getName(position);
        setIntent(new Intent(getActivity(), DetailActivity.class)
                .putExtra(FBNAMES, name));

        getIntent().putExtra("Location", customListAdapter.getLocation(position));
        getIntent().putExtra(FBPICS, customListAdapter.getID(position));
        getIntent().putExtra("latLng", customListAdapter.getLatLng(position));
        startActivity(getIntent());
    }

    private void fbPermissionsLogin(View view) {
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("user_friends"));
    }

    private Location getLocation() {
        List<String> AllProviders = locationManager.getProviders(true);
        Location bestAccuracy = null;
        for (String provider : AllProviders) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location == null) {
                continue;
            }
            if (bestAccuracy == null || location.getAccuracy() < bestAccuracy.getAccuracy()) {
                bestAccuracy = location;
            }
        }
        if (bestAccuracy == null) {
            return null;
        }
        return  bestAccuracy;

    }


    @Override
    public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session.getState());
        }
        uiHelper.onResume();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);


    }


    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void onSessionStateChange(SessionState state) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in!");


        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out!");
        }
    }


    public ListView getResultsAdapter() {
        return resultsList;
    }

    public void setResultsAdapter(ArrayList<FBFriendDetails> profileName) {

        setAdapterAndIntents(view,profileName);
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }


}









