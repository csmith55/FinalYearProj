package app.com.project.csmith.finalyearproject;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "MainFragment";
    private static final String FBPICS = "FBPICS";
    private static final String FBNAMES = "FBNAMES";
    private static String[] fakeLocations = {"BT10 0GR \nDistance: 100", "CastleCourt \nDistance: 150", "Lisburn \nDistance: 600", "BT7 1LQ \nDistance: 1500", "BT25 3PL \nDistance: 2500", "Aviva Stadium \nDistance: 3500"};
    protected ArrayAdapter<String> profileName;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    private UiLifecycleHelper uiHelper;
    private ArrayList<String> profilePics = new ArrayList<String>();
    private LocationManager locationManager;
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
        String locationProvider = LocationManager.GPS_PROVIDER;
       locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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

        locationManager.requestLocationUpdates(locationProvider,0,0,locationListener);

        Location lastKnown = locationManager.getLastKnownLocation(locationProvider);
        //When finished!!
       // locationManager.removeUpdates(locationListener);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        if(lastKnown!=null) {
            try {
                addresses = geocoder.getFromLocation(lastKnown.getLatitude(), lastKnown.getLongitude(), 1);

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


        locationManager.removeUpdates(locationListener);


        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("user_friends"));

        // Find the user's profile picture custom view
        // Get a reference to the ListView, and attach this adapter to it.

        ListView listView = (ListView) view.findViewById(R.id.listview_friend);
        listView.setAdapter(profileName);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String name = profileName.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(FBNAMES, name);
                intent.putExtra(FBPICS, profilePics.get(position));
                startActivity(intent);
            }
        });


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
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

    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in!");
            updateFriends();

        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out!");
        }
    }

    public void makeMeRequest(final Session session) {
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
                                ProfilePictureView profilePictureView = null;

                                if (type.contains("Nearest Friend")) {
                                    int numOfFriends = Integer.parseInt(value);
                                    if (numOfFriends > graphUsers.size())
                                        numOfFriends = graphUsers.size();
                                    for (int i = 0; i < numOfFriends; i++) {


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


                                        profilePictureView.setProfileId(graphUsers.get(i).getId());
                                        profilePictureView.setCropped(true);

                                    }
                                } else {
                                    for (int i = 0; i < graphUsers.size(); i++) {

                                        String[] split = fakeLocations[i].split("Distance: ");
                                        String distance = split[1];
                                        int distanceOfFriend = Integer.parseInt(distance);
                                        int maxDistance = Integer.parseInt(value);
                                        if (distanceOfFriend <= maxDistance) {
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


                                            profilePictureView.setProfileId(graphUsers.get(i).getId());
                                            profilePictureView.setCropped(true);
                                        }

                                    }
                                }
                            }
                        }
                        if (response.getError() != null) {
                            // Handle errors, will do so later.
                        }
                    }


                });
        request.executeAsync();


    }

}










