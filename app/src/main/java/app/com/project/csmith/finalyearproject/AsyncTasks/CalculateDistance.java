package app.com.project.csmith.finalyearproject.AsyncTasks;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

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
import java.util.Date;

import app.com.project.csmith.finalyearproject.UIPermissions.MainFragment;
import app.com.project.csmith.finalyearproject.Utilities.CustomCompator;
import app.com.project.csmith.finalyearproject.Utilities.UrlUtility;

/**
 * Created by csmith on 02/03/15.
 */
public class CalculateDistance extends AsyncTask<Void, Void, Void> {

    private final MainFragment mainFragment;
    private ArrayList<FBFriendDetails> friendDetails;
    private final ArrayList<FBFriendDetails> actualFBFriends, resultFriends;
    private LatLng usersLatLng;
    private String type, measurement;
    private int value;
    private ProgressDialog progress;

    public CalculateDistance(ArrayList<FBFriendDetails> friendDetails, MainFragment mainFragment, LatLng usersLatLng, ProgressDialog progress) {
        this.friendDetails = friendDetails;
        this.mainFragment = mainFragment;
        this.usersLatLng = usersLatLng;
        this.progress = progress;
        this.actualFBFriends = new ArrayList<>();
        resultFriends = new ArrayList<>();
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(mainFragment.getActivity());
        type = preference.getString("type", "Nearest");
        value = Integer.parseInt(preference.getString("value", "3"));
        measurement = preference.getString("measurements", "Km");
       rtreeQuery(5);
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (FBFriendDetails details : friendDetails) {
            String jsonDetailsString = UrlUtility.makeConnection(details.getLatLng(), usersLatLng);
            try {
                getDetailsFromJson(jsonDetailsString, details);
            } catch (JSONException e) {
                Log.e("", e.getMessage(), e);
                e.printStackTrace();
            }
        }

        return null;
    }

    private Void getDetailsFromJson(String jsonDetailsString, FBFriendDetails details) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonDetailsString);
        if (jsonObject.has("rows")) {
            JSONArray rows = jsonObject.getJSONArray("rows");

            //rows.getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getDouble("value")
            JSONArray element = rows.getJSONObject(0).getJSONArray("elements");
            Log.d("SFHAUISFHIUAF", element.toString());
            details.setDistanceText(element.getJSONObject(0).getJSONObject("distance").getString("text"));
            details.setMetricDistance(element.getJSONObject(0).getJSONObject("distance").getDouble("value"));
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aDouble) {

        //check what type of query then filter out the results
        Collections.sort(friendDetails, new CustomCompator());
        if (type.contains("Nearest")) {
            nearestFriendsQuery();
        } else rangeFriendsQuery();


    }

    private void rangeFriendsQuery() {
        double maxDistance = value;


        for (int i = 0; i < friendDetails.size(); i++) {
            final FBFriendDetails friend = friendDetails.get(i);
            if (withinDateRange(friend)) {
                if (measurement.contains("Miles")) {
                    if (friend.getImperialDistance() <= maxDistance) {
                        friend.setDistanceText(String.format("%.2f", friend.getImperialDistance()) + " miles");
                        resultFriends.add(friend);

                    }
                } else {
                    if (friend.getMetricDistance() <= maxDistance) {
                        friend.setDistanceText(String.format("%.2f", friend.getMetricDistance()) + " km");
                        resultFriends.add(friend);
                    }
                }
            }
        }
        addFriendToResults();
    }

    private boolean withinDateRange(FBFriendDetails friend) {
        Date fourHoursAgo = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 4);
        return  friend.getDate().after(fourHoursAgo);
    }


    private void nearestFriendsQuery() {
        int numOfFriends = value;
        if (numOfFriends > friendDetails.size())
            numOfFriends = friendDetails.size();

        for (int i = 0; i < numOfFriends; i++) {
            final FBFriendDetails friend = friendDetails.get(i);
            if (withinDateRange(friend)) {
                if (measurement.equals("Miles")) {
                    friend.setDistanceText(String.format("%.2f", friend.getImperialDistance()) + " miles");
                } else {
                    friend.setDistanceText(String.format("%.2f", friend.getMetricDistance()) + " km");
                }
                resultFriends.add(friend);

            }
        }
        addFriendToResults();

    }

    private void rtreeQuery(int range) {
        int intValue;
        boolean isNearest =false;
        if(type.contains("Range")) {
            intValue = value;
        }else {
            isNearest = true;
            if(value > friendDetails.size())
            value = friendDetails.size();
            intValue = range;
        }
        SpatialIndex si = new RTree();
        si.init(null);

        float distanceSearchBase = (float) ((intValue) / 111.0);
        final ArrayList<Rectangle> rects = createRectangles(intValue, si, distanceSearchBase);


        final Point p = new Point((float) usersLatLng.latitude, (float) usersLatLng.longitude);

        for(int i = 0; i < rects.size(); i++){
            checkIntersection(i,rects,p);
        }



        if(isNearest && actualFBFriends.size() < value){
            intValue = intValue * 2;
            actualFBFriends.clear();
            rtreeQuery(intValue);
        }

        friendDetails = actualFBFriends;

    }

    private void checkIntersection(int i, ArrayList<Rectangle> rects, Point p) {
        float x, y;
        for (int j = 0; j < friendDetails.size(); j++) {
            if(actualFBFriends.size() == value) break;
            x = (float) friendDetails.get(j).getLatLng().latitude;
            y = (float) friendDetails.get(j).getLatLng().longitude;
            Rectangle rectangle = new Rectangle(x, y, x + 0.001f, y + 0.001f);
            if (rects.get(i).intersects(rectangle))
            {
                if(!actualFBFriends.contains(friendDetails.get(j))){
                actualFBFriends.add(friendDetails.get(j));

            }
            }



        }
    }

    private ArrayList<Rectangle> createRectangles(double value, SpatialIndex si, float distanceSearchBase) {
       ArrayList<Rectangle> rects = new ArrayList<>();

        int id = 0;
        for (float row = (float) (usersLatLng.latitude - distanceSearchBase); row < usersLatLng.latitude + distanceSearchBase; row += 0.01f)
            for (float column = (float) (usersLatLng.longitude - distanceSearchBase); column < usersLatLng.longitude + distanceSearchBase; column += 0.01f) {
                Rectangle rectangle = new Rectangle(row, column, row + 0.01f, column + 0.01f);
                rects.add(rectangle);

                si.add(rectangle, id++); //

            }
        return rects;
    }

    private void addFriendToResults() {
            mainFragment.setResultsAdapter(resultFriends);
        mainFragment.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                progress.dismiss();
            }
        });
    }



}
