package app.com.project.csmith.finalyearproject.LocationTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.csmith.myapplication.backend.myApi.MyApi;
import com.example.csmith.myapplication.backend.myApi.model.LocationDetails;
import com.facebook.model.GraphUser;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.com.project.csmith.finalyearproject.DistanceTasks.CalculateDistance;
import app.com.project.csmith.finalyearproject.DistanceTasks.FBFriendDetails;
import app.com.project.csmith.finalyearproject.HomeUI.MainFragment;
import app.com.project.csmith.finalyearproject.Utilities.ApiBuilder;


public class GetLocationAsyncTask extends AsyncTask<Void, Void, Void> {
    private static MyApi myApiService = null;
    private List<GraphUser> graphUsers;
    private ArrayList<FBFriendDetails> friendDetails;
    private MainFragment mainFragment;
    private ProgressDialog progress;
    private LatLng usersLatLng;


    public GetLocationAsyncTask(List<GraphUser> graphUsers, ArrayList<FBFriendDetails> friendDetails, MainFragment mainFragment, LatLng latLng) {
        this.graphUsers = graphUsers;
        this.mainFragment = mainFragment;
        this.usersLatLng = latLng;
        this.friendDetails = friendDetails;
        progress = new ProgressDialog(mainFragment.getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Calculating Distances...");

    }

    /**
     * Calls the API backend to retrieve the user's location
     * Adds the result to friendDetails list
     * @param params of void
     * @return void
     */

    @Override
    protected Void doInBackground(Void... params) {
        mainFragment.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                progress.show();
            }
        });

       myApiService = ApiBuilder.buildApi(myApiService);
       friendDetails.clear();

        for (int i = 0; i < graphUsers.size(); i++) {
            try {
                LocationDetails locationDetails = myApiService.getLocation(graphUsers.get(i).getId()).execute().getData();
                friendDetails.add(new FBFriendDetails(graphUsers.get(i).getId(), graphUsers.get(i).getName(),
                        new LatLng(locationDetails.getLat(), locationDetails.getLng()),
                       new Date(locationDetails.getDate().getValue())));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    /**
     * Starts after the doInBackground
     * Calls calculate distance with all the retrieved locations
     * @param aVoid void
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        if (!friendDetails.isEmpty()) {
            new CalculateDistance(friendDetails, mainFragment, usersLatLng,progress).execute();
        } else{
            Toast.makeText(mainFragment.getActivity(), "Press the top right icon to get started!", Toast.LENGTH_LONG).show();
        }

    }

}