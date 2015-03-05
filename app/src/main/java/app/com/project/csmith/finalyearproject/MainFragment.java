package app.com.project.csmith.finalyearproject;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import com.google.android.gms.maps.model.LatLng;

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
    private ArrayAdapter<String> profileName;
    private ArrayList<FBFriendDetails> friendDetails = new ArrayList<>();
    private UiLifecycleHelper uiHelper;
    private String usersFacebookId;
    private MainFragment mainFragment = this;

    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        updateFriends();


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
        if (Session.getActiveSession().isOpened())
        {
            // Request user data and show the results
            Request.newMeRequest(Session.getActiveSession(), new Request.GraphUserCallback()
            {
                @Override
                public void onCompleted(GraphUser user, Response response)
                {
                    if (null != user)
                    {

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
                            if (graphUsers != null) {
                                new GetLocationAsyncTask(graphUsers,friendDetails,mainFragment,new LatLng(getLocation().getLatitude(),getLocation().getLongitude())).execute();
                            }
                        }

                    }
                });
        request.executeAsync();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onStart() {
        super.onStart();



    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        setProfileName(new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.list_item_friend_textview, new ArrayList<String>()));
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
        listView.setAdapter(getProfileName());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                createDetailActivityIntent(position);
            }
        });
    }

    private void createDetailActivityIntent(int position) {
        String name = getProfileName().getItem(position);
         setIntent(new Intent(getActivity(), DetailActivity.class)
                .putExtra(FBNAMES, name));
        getIntent().putExtra(FBPICS, friendDetails.get(position).getID());
        getIntent().putExtra("latLng", friendDetails.get(position).getLatLng());
        startActivity(getIntent());
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
                if(usersFacebookId != null)
               new EndpointsAsyncTask(usersFacebookId).execute(new Pair<Context, LatLng>(getActivity(), new LatLng(location.getLatitude(),location.getLongitude())));

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


        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out!");
        }
    }






    public ArrayAdapter<String> getProfileName() {
        return profileName;
    }

    public void setProfileName(ArrayAdapter<String> profileName) {
        this.profileName = profileName;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }


}










