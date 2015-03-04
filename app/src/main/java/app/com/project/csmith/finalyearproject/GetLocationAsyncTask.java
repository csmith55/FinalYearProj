package app.com.project.csmith.finalyearproject;

import android.content.Context;
import android.os.AsyncTask;

import com.example.csmith.myapplication.backend.myApi.MyApi;
import com.facebook.model.GraphUser;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csmith on 22/02/15.
 */
 public class GetLocationAsyncTask extends AsyncTask<Void,Void,Void> {
    private static MyApi myApiService = null;
    private List<GraphUser> graphUsers;
    private ArrayList<FBFriendDetails> friendDetails;
    private MainFragment mainFragment;
    private int index;


    private Context context;
    private LatLng usersLatLng;



    public GetLocationAsyncTask(List<GraphUser> graphUsers, MainFragment mainFragment, LatLng latLng, Context context) {
        this.graphUsers = graphUsers;
        this.mainFragment = mainFragment;
        this.usersLatLng = latLng;
        this.context = context;
        friendDetails = new ArrayList<>(graphUsers.size());
    }


    @Override
    protected Void doInBackground(Void... params) {
        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://finalyearproject40057321.appspot.com/_ah/api/");


            myApiService = builder.build();
        }

        for (int i = 0; i < graphUsers.size(); i++){
            try {
                List<Double> list = myApiService.getLocation(graphUsers.get(i).getId()).execute().getData();
                friendDetails.add(new FBFriendDetails(graphUsers.get(i).getId(),graphUsers.get(i).getName(),new LatLng(list.get(0),list.get(1))));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid){

            new CalculateDistance(friendDetails,mainFragment,context).execute();
            //addFriendToResults(latLng);

    }

   /* private void addFriendToResults(LatLng longLat) {
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

        mainFragment.getProfileName().add(graphUsers.getName() + "\nLocation: " + addresses.get(0).getAddressLine(0));

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
        profilePictureView.setProfileId(graphUsers.getID());
        profilePictureView.setCropped(true);

    }*/
}