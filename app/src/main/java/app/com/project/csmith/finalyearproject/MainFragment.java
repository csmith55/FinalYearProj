package app.com.project.csmith.finalyearproject;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "MainFragment";
    private static final String FBPICS = "FBPICS";
    private static final String FBNAMES = "FBNAMES";
    private static final String[] fakeLocations = {"BT10 0GR \nDistance: 100", "CastleCourt \nDistance: 150", "Lisburn \nDistance: 600", "BT7 1LQ \nDistance: 1500", "BT25 3PL \nDistance: 2500", "Aviva Stadium \nDistance: 3500"};
    private final Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(state);
        }
    };
    private final ArrayList<String> profilePics = new ArrayList<>();
    private ArrayAdapter<String> profileName;
    private UiLifecycleHelper uiHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void updateFriends() {
        makeMeRequest(Session.getActiveSession());
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onStart() {
        super.onStart();
        updateFriends();

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        profileName = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.list_item_friend_textview, new ArrayList<String>());
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        final Location lastKnown = getLocation();

        fbPermissionsLogin(view);

        // Find the user's profile picture custom view
        // Get a reference to the ListView, and attach this adapter to it.

        setAdapterAndIntents(view, lastKnown);


        return view;

    }

    private void setAdapterAndIntents(View view, final Location lastKnown) {
        ListView listView = (ListView) view.findViewById(R.id.listview_friend);
        listView.setAdapter(profileName);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                createDetailActivityIntent(position, lastKnown);
            }
        });
    }

    private void createDetailActivityIntent(int position, Location lastKnown) {
        String name = profileName.getItem(position);
        Intent intent = new Intent(getActivity(), DetailActivity.class)
                .putExtra(FBNAMES, name);
        intent.putExtra(FBPICS, profilePics.get(position));
        intent.putExtra("UserLng", lastKnown.getLongitude());
        intent.putExtra("UserLat", lastKnown.getLatitude());
        startActivity(intent);
    }

    private void fbPermissionsLogin(View view) {
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("user_friends"));
    }

    private Location getLocation() {
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateFriends();
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

        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

        final Location lastKnown = locationManager.getLastKnownLocation(locationProvider);
        //When finished!!
        // locationManager.removeUpdates(locationListener);


        locationManager.removeUpdates(locationListener);
        return lastKnown;
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
            updateFriends();

        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out!");
        }
    }

    private void makeMeRequest(final Session session) {
        // Make an API call to get user data and define a
        // new callback to handle the response.
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String value = preference.getString("value", "10000");
        final String type = preference.getString("units", "default value");

        Request request = Request.newMyFriendsRequest(session,
                new Request.GraphUserListCallback() {


                    @Override
                    public void onCompleted(List<GraphUser> graphUsers, Response response) {
                        if (session == Session.getActiveSession()) {
                            if (graphUsers != null) {

                                // Set the id for the ProfilePictureView
                                // view that in turn displays the profile picture.
                                //  profilePictureView.setProfileId(graphUsers.get(0).getId());
                                // Set the Textview's text to the user's name.
                                // userNameView.setText(user.getName());
                                profileName.clear();
                                profilePics.clear();


                                if (type.contains("Nearest Friend")) {
                                    nearestFriendsQuery(graphUsers, value);
                                } else {
                                    rangeFriendsQuery(graphUsers, value);
                                }
                            }
                        }

                    }


                });
        request.executeAsync();


    }

    private void rangeFriendsQuery(List<GraphUser> graphUsers, String value) {

        for (int i = 0; i < graphUsers.size(); i++) {

            String[] split = fakeLocations[i].split("Distance: ");
            String distance = split[1];
            int distanceOfFriend = Integer.parseInt(distance);
            int maxDistance = Integer.parseInt(value);
            if (distanceOfFriend <= maxDistance) {
                addFriendToResults(graphUsers, i);
            }

        }
    }

    private void addFriendToResults(List<GraphUser> graphUsers, int i) {
        ProfilePictureView profilePictureView = null;
        profileName.add(graphUsers.get(i).getName() + "\nLocation: " + fakeLocations[i]);
        profilePics.add(graphUsers.get(i).getId());

        switch (i) {
            case 0:
                profilePictureView = (ProfilePictureView) getView().findViewById(R.id.profile_pic0);
                break;
            case 1:
                profilePictureView = (ProfilePictureView) getView().findViewById(R.id.profile_pic1);
                break;
            case 2:
                profilePictureView = (ProfilePictureView) getView().findViewById(R.id.profile_pic2);
                break;
            case 3:
                profilePictureView = (ProfilePictureView) getView().findViewById(R.id.profile_pic3);
                break;
            case 4:
                profilePictureView = (ProfilePictureView) getView().findViewById(R.id.profile_pic4);
                break;
        }


        assert profilePictureView != null;
        profilePictureView.setProfileId(graphUsers.get(i).getId());
        profilePictureView.setCropped(true);

    }

    private void nearestFriendsQuery(List<GraphUser> graphUsers, String value) {

        int numOfFriends = Integer.parseInt(value);
        if (numOfFriends > graphUsers.size())
            numOfFriends = graphUsers.size();
        for (int i = 0; i < numOfFriends; i++) {
            addFriendToResults(graphUsers, i);
        }
    }

}










