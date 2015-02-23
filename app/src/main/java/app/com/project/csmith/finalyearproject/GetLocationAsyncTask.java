package app.com.project.csmith.finalyearproject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.example.csmith.myapplication.backend.myApi.MyApi;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by csmith on 22/02/15.
 */
public class GetLocationAsyncTask extends AsyncTask<Pair<Context, String>, Void,LatLng> {
    private static MyApi myApiService = null;
    private GraphUser graphUser;
    private MainFragment mainFragment;
    private int index;


    private Context context;



    public GetLocationAsyncTask(GraphUser graphUser, int i, MainFragment mainFragment) {
        this.graphUser = graphUser;
        this.index = i;
        this.mainFragment = mainFragment;
    }


    @Override
    protected LatLng doInBackground(Pair<Context, String>... params) {
        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://finalyearproject40057321.appspot.com/_ah/api/");


            myApiService = builder.build();
        }

        context = params[0].first;
        String id = params[0].second;
        Log.d("id", id);


        try {

           List<Double> list=  myApiService.getLocation(params[0].second).execute().getData();
            return new LatLng(list.get(0),list.get(1));
        } catch (IOException e) {
            return null;
        }


    }

    @Override
    protected void onPostExecute(LatLng latLng){
        if(latLng != null) {
            mainFragment.setLatLng(latLng);
            addFriendToResults(latLng);
        }
    }

    private void addFriendToResults(LatLng longLat) {
        ProfilePictureView profilePictureView = null;
        //geocoder here!
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(longLat.latitude, longLat.longitude, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

        mainFragment.getProfileName().add(graphUser.getName() + "\nLocation: " + addresses.get(0).getAddressLine(0));
        mainFragment.getProfilePics().add(graphUser.getId());

        switch (index) {
            case 0:
                profilePictureView = (ProfilePictureView) mainFragment.getView().findViewById(R.id.profile_pic0);
                break;
            case 1:
                profilePictureView = (ProfilePictureView) mainFragment.getView().findViewById(R.id.profile_pic1);
                break;
            case 2:
                profilePictureView = (ProfilePictureView) mainFragment.getView().findViewById(R.id.profile_pic2);
                break;
            case 3:
                profilePictureView = (ProfilePictureView) mainFragment.getView().findViewById(R.id.profile_pic3);
                break;
            case 4:
                profilePictureView = (ProfilePictureView) mainFragment.getView().findViewById(R.id.profile_pic4);
                break;
        }


        assert profilePictureView != null;
        profilePictureView.setProfileId(graphUser.getId());
        profilePictureView.setCropped(true);

    }
}