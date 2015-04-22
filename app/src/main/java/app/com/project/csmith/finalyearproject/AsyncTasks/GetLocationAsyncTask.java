package app.com.project.csmith.finalyearproject.AsyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.csmith.myapplication.backend.myApi.MyApi;
import com.example.csmith.myapplication.backend.myApi.model.LocationDetails;
import com.facebook.model.GraphUser;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.com.project.csmith.finalyearproject.UIPermissions.MainFragment;

/**
 * Created by csmith on 22/02/15.
 */
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

    @Override
    protected void onPostExecute(Void aVoid) {
        if (!friendDetails.isEmpty()) {
            new CalculateDistance(friendDetails, mainFragment, usersLatLng,progress).execute();
        }

    }

}