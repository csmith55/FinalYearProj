package app.com.project.csmith.finalyearproject.DistanceTasks;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

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

import app.com.project.csmith.finalyearproject.HomeUI.MainFragment;
import app.com.project.csmith.finalyearproject.Utilities.DistanceComparator;
import app.com.project.csmith.finalyearproject.Utilities.UrlUtility;

public class CalculateDistance extends AsyncTask<Void, Void, Void> {

    private final MainFragment mainFragment;
    private ArrayList<FBFriendDetails> friendDetails;
    private final ArrayList<FBFriendDetails> actualFBFriends, resultFriends;
    private LatLng usersLatLng;
    private String type, measurement;
    private int value;
    private ProgressDialog progress;
    private boolean isNearest;



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
        withinDateRange();
        rtreeQuery(5);
    }

    /**
     *This method overrides from abstract method in AsyncTask
     * Cycles through the friendDetails list passed the latitude and longitude to UrlUtility makeConnection
     *Passes the JSON result returned from makeConnection into getDetailsFromJson
     * @param params is a varargs of voids
     * @return void
     */

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

    /**
     * Executed from doInBackground.
     * Parses the JSON result and stores it within details
     * @param jsonDetailsString Complete JSON result that needs to ne parsed
     * @param details FBFriendDetails object that stores the value of the parsing
     * @return void
     *
     */

    private Void getDetailsFromJson(String jsonDetailsString, FBFriendDetails details) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonDetailsString);
        if (jsonObject.has("rows")) {
            JSONArray rows = jsonObject.getJSONArray("rows");

            JSONArray element = rows.getJSONObject(0).getJSONArray("elements");
            details.setDistanceText(element.getJSONObject(0).getJSONObject("distance").getString("text"));
            details.setMetricDistance(element.getJSONObject(0).getJSONObject("distance").getDouble("value"));
        }
        return null;

    }

    /**
     *Uses custom sort to arrange list by distance in ascending order
     * @param aVoid varargs of Void
     */

    @Override
    protected void onPostExecute(Void aVoid) {

        //check what type of query then filter out the results
        Collections.sort(friendDetails, new DistanceComparator());
        if (type.contains("Nearest")) { // checks shared preference for query type selected
            nearestFriendsQuery();
        } else rangeFriendsQuery();


    }

    /**
     * Executes the range friends query
     * Compares the distance set by the user and compares to unit of user's choice
     * Checks if friend is within the distance set
     */
    private void rangeFriendsQuery() {
        double maxDistance = value;


        for (int i = 0; i < friendDetails.size(); i++) {
            final FBFriendDetails friend = friendDetails.get(i);

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

        addFriendToResults();
    }

    /**
     *Compares if the date located retrieved from the datastore is recent
     * recent in this case is four hours but can be easily changed
     */
    private void withinDateRange() {
        Date fourHoursAgo = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 4);
        for (int i = 0; i < friendDetails.size(); i++) {
            if (friendDetails.get(i).getDate().before(fourHoursAgo)) {
                friendDetails.remove(i--);
            }
        }
    }

    /**
     * This method is used to compare the nearest friends of the user
     * Any friends found are added to the results list
     */
    private void nearestFriendsQuery() {
        int numOfFriends = value;
        // if the user has inputted a higher value than the amount of friends they have
        //the inputted value will be set to the size of the friends they have
        if (numOfFriends > friendDetails.size())
            numOfFriends = friendDetails.size();

        //loop through the number of friends
        for (int i = 0; i < numOfFriends; i++) {
            final FBFriendDetails friend = friendDetails.get(i);
            if (measurement.equals("Miles")) { //check the unit of measurement and set the test based on that
                friend.setDistanceText(String.format("%.2f", friend.getImperialDistance()) + " miles");
            } else {
                friend.setDistanceText(String.format("%.2f", friend.getMetricDistance()) + " km");
            }
            resultFriends.add(friend);

        }

        addFriendToResults();

    }

    /**
     * Used to create a search base around the user's location, made up of rectangles.
     * Then checks for intersection of friends within the search base
     * @param range the span of the area to be searched
     */
    private void rtreeQuery(int range) {
        int intValue;
        isNearest = false;
        if (type.contains("Range")) {
            intValue = value;
        } else {
            //if nearest friends and value is greater then reset the value to correct size
            isNearest = true;
            if (value > friendDetails.size())
                value = friendDetails.size();
            intValue = range;
        }
        SpatialIndex si = new RTree();
        si.init(null);

        // the search base is calculated by taking the value and dividing it by 111.
        // 111 is chosen due to 1 degree of latitude = approx 111 km
        // by dividing by 111 we can discover how many  latitude and longitude degrees is required
        float distanceSearchBase = (float) ((intValue) / 111.0);
        final ArrayList<Rectangle> rects = createRectangles(intValue, si, distanceSearchBase);


        final Point p = new Point((float) usersLatLng.latitude, (float) usersLatLng.longitude);

        for (int i = 0; i < rects.size(); i++) {
            checkIntersection(i, rects, p);
        }

        //if is nearest query and we have not found enough friends for the user
        //Reset the list and double the search base
        if (isNearest && actualFBFriends.size() < value) {
            intValue = intValue * 2;
            actualFBFriends.clear();
            rtreeQuery(intValue);
        }

        friendDetails = actualFBFriends;

    }

    /**
     * Determines if a friend is within the search base.
     * Rectangle of the friend's location is created and
     * intersects checks if it intersects with user's bounding rectangles
     * @param i rectangle to search inside
     * @param rects arraylist of all the bounding rectangles
     * @param p point of the user
     */
    private void checkIntersection(int i, ArrayList<Rectangle> rects, Point p) {
        float x, y;
        for (int j = 0; j < friendDetails.size(); j++) {
            // creates a rectangle using each friends latitude and longitude
            //uses intersect method to see if it lies within out search base
            x = (float) friendDetails.get(j).getLatLng().latitude;
            y = (float) friendDetails.get(j).getLatLng().longitude;
            Rectangle rectangle = new Rectangle(x, y, x + 0.001f, y + 0.001f);
            if (rects.get(i).intersects(rectangle)) {
                if (!actualFBFriends.contains(friendDetails.get(j))) {
                    actualFBFriends.add(friendDetails.get(j));

                }
            }

        }
    }

    /**
     * Creates the bounding rectangles around the user's latitude & longitude location
     * @param value value entered by the user
     * @param si spatial index to add the rectangles
     * @param distanceSearchBase distance to be searched
     * @return arraylist of created rectangles
     */
    private ArrayList<Rectangle> createRectangles(double value, SpatialIndex si, float distanceSearchBase) {
        ArrayList<Rectangle> rects = new ArrayList<>();

        int id = 0;
        for (float row = (float) (usersLatLng.latitude - distanceSearchBase); row < usersLatLng.latitude + distanceSearchBase; row += 0.01f)
            for (float column = (float) (usersLatLng.longitude - distanceSearchBase); column < usersLatLng.longitude + distanceSearchBase; column += 0.01f) {
               //creates rectangles around the user
                Rectangle rectangle = new Rectangle(row, column, row + 0.01f, column + 0.01f);
                rects.add(rectangle);
                si.add(rectangle, id++);
            }
        return rects;
    }

    private void addFriendToResults() {
        if (resultFriends.isEmpty()) {
            Toast.makeText(mainFragment.getActivity(), "No friends found", Toast.LENGTH_LONG).show();
        } else {
            mainFragment.setResultsAdapter(resultFriends);
            Toast.makeText(mainFragment.getActivity(), "Found friends!", Toast.LENGTH_LONG).show();
        }

        mainFragment.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                progress.dismiss();
            }
        });
    }


}
