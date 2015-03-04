package app.com.project.csmith.finalyearproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.facebook.model.GraphUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csmith on 02/03/15.
 */
public class CalculateDistance extends AsyncTask<Void,Void,Void> {

    private final MainFragment mainFragment;
    private final Context context;
    private final ArrayList<FBFriendDetails> friendDetails;
    private ArrayList<Double> distances;


    public CalculateDistance(ArrayList<FBFriendDetails> friendDetails, MainFragment mainFragment, Context context) {
        this.friendDetails = friendDetails;
        this.mainFragment = mainFragment;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        for(FBFriendDetails details : friendDetails) {
            String jsonDetailsString = UrlUtility.makeConnection(details.getLatLng());
            try {
               details.setDistance(getDetailsFromJson(jsonDetailsString));
            } catch (JSONException e) {
                Log.e("", e.getMessage(), e);
                e.printStackTrace();
            }
        }

        return null;
    }

    private Double getDetailsFromJson(String jsonDetailsString) throws JSONException{
        JSONObject jsonObject = new JSONObject(jsonDetailsString);
        JSONArray rows = jsonObject.getJSONArray("rows");

        //rows.getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getDouble("value")
        JSONArray element = rows.getJSONObject(0).getJSONArray("elements");
        Log.d("SFHAUISFHIUAF", element.toString());
            return element.getJSONObject(0).getJSONObject("distance").getDouble("value");

    }

    @Override
    protected void onPostExecute(Void aDouble) {

       //check what type of query then filter out the results

    }

    private static void rangeFriendsQuery(List<GraphUser> graphUsers, String value) {

        for (int i = 0; i < graphUsers.size(); i++) {


            int distanceOfFriend = 500;
            int maxDistance = Integer.parseInt(value);
            if (distanceOfFriend <= maxDistance) {
               // friendDetails.add(new FBFriendDetails(graphUsers.get(i).getId(),graphUsers.get(i).getName(), new LatLng(list.get(0), list.get(1))));

            }

        }
    }

/*

    private static void nearestFriendsQuery(List<GraphUser> graphUsers, String value) {

        int numOfFriends = Integer.parseInt(value);
        if (numOfFriends > graphUsers.size())
            numOfFriends = graphUsers.size();
        for (int i = 0; i < numOfFriends; i++) {
            new GetLocationAsyncTask(graphUsers.get(i), i, this, new LatLng(getLocation().getLatitude(),getLocation().getLongitude())).execute(new Pair<Context, String>(getActivity(), graphUsers.get(i).getId()));

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
        profilePictureView.setProfileId(graphUser.getID());
        profilePictureView.setCropped(true);

    }*/


}
