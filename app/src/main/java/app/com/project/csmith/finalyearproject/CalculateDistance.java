package app.com.project.csmith.finalyearproject;

import android.content.SharedPreferences;
import android.location.Address;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.model.LatLng;
import com.infomatiq.jsi.Point;
import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.SpatialIndex;
import com.infomatiq.jsi.rtree.RTree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gnu.trove.TIntProcedure;

/**
 * Created by csmith on 02/03/15.
 */
public class CalculateDistance extends AsyncTask<Void,Void,Void> {

    private final MainFragment mainFragment;
    private final ArrayList<FBFriendDetails> friendDetails;

    private LatLng usersLatLng;
    private final ArrayList<FBFriendDetails> actualFBFriends;



    public CalculateDistance(ArrayList<FBFriendDetails> friendDetails, MainFragment mainFragment, LatLng usersLatLng) {
        this.friendDetails = friendDetails;
        this.mainFragment = mainFragment;
        this.usersLatLng = usersLatLng;

        this.actualFBFriends = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... params) {
        for(FBFriendDetails details : friendDetails) {
            String jsonDetailsString = UrlUtility.makeConnection(details.getLatLng(),usersLatLng);
            try {
               getDetailsFromJson(jsonDetailsString, details);
            } catch (JSONException e) {
                Log.e("", e.getMessage(), e);
                e.printStackTrace();
            }
        }

        return null;
    }

    private Void getDetailsFromJson(String jsonDetailsString, FBFriendDetails details) throws JSONException{
        JSONObject jsonObject = new JSONObject(jsonDetailsString);
        if (jsonObject.has("rows")) {
            JSONArray rows = jsonObject.getJSONArray("rows");

            //rows.getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getDouble("value")
            JSONArray element = rows.getJSONObject(0).getJSONArray("elements");
            Log.d("SFHAUISFHIUAF", element.toString());
            details.setDistanceText(element.getJSONObject(0).getJSONObject("distance").getString("text"));
            details.setDistance(element.getJSONObject(0).getJSONObject("distance").getDouble("value"));
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aDouble) {

       //check what type of query then filter out the results
        Collections.sort(friendDetails, new CustomCompator());


        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(mainFragment.getActivity());
        final String value = preference.getString("value", "10000");
        final String type = preference.getString("units", "default value");

        for(int i = 0; i < friendDetails.size(); i++){
            addFriendToResults(friendDetails.get(i),i);
        }






    }

    private void rangeFriendsQuery(String value) {
        Collections.sort(friendDetails, new CustomCompator());
        int maxDistance = Integer.parseInt(value);

        for (int i = 0; i < friendDetails.size(); i++) {


            if (friendDetails.get(i).getDistance() <= maxDistance) {

                addFriendToResults(friendDetails.get(i), i);

            }

        }
    }


    private void nearestFriendsQuery(String value) {
        Collections.sort(friendDetails, new CustomCompator());

        int numOfFriends = Integer.parseInt(value);
        if (numOfFriends > friendDetails.size())
            numOfFriends = friendDetails.size();

        for (int i = 0; i < numOfFriends; i++){
            addFriendToResults(friendDetails.get(i),i);
        }

    }

    protected void rtreeQuery(final String value){


        SpatialIndex si = new RTree();
        si.init(null);


        final Rectangle[] rects = new Rectangle[100000];
        int id = 0;
        for (float row = (float) (usersLatLng.latitude-1); row < usersLatLng.latitude+1; row+=0.01f)
            for (float column  = (float) (usersLatLng.longitude-1); column < usersLatLng.longitude+1; column+=0.01f) {
                rects[id] = new Rectangle(row, column, row+0.01f, column+0.01f);
                System.out.println("Rectangle" + id + " " + rects[id]);
                si.add(rects[id],id); //
                id++;
            }


        final Point p = new Point((float)usersLatLng.latitude, (float)usersLatLng.longitude);
        int index = 0;
        si.nearestN(p, new TIntProcedure() {
            public boolean execute(int i) {
                Log.d("Tag","Rectangle " + i + " " + rects[i] + ", distance=" + rects[i].distance(p));
                float x,y;

                for(int j = 0; j < friendDetails.size(); j++){
                    x = (float) friendDetails.get(j).getLatLng().latitude;
                    y = (float) friendDetails.get(j).getLatLng().longitude;
                    Rectangle rectangle = new Rectangle(x,y,x+0.01f,y+0.01f);
                    if(rects[i].intersects(rectangle)){
                        actualFBFriends.add(friendDetails.get(j));
                    }
                }
                return true;
            }
        }, 20, Float.MAX_VALUE);

        new CalculateDistance(actualFBFriends,mainFragment,usersLatLng).execute();

    }

    private void addFriendToResults(FBFriendDetails details, int index) {
        ProfilePictureView profilePictureView = null;

        List<Address> addresses = GeocoderUtil.convertLatLngToAddress(details.getLatLng(),mainFragment.getActivity());
        mainFragment.getProfileName().add(details.getName() + "\nLocation: " + addresses.get(0).getAddressLine(0) + "\nDistance: " + details.getDistanceText());


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
        profilePictureView.setProfileId(details.getID());
        profilePictureView.setCropped(true);

    }


}
