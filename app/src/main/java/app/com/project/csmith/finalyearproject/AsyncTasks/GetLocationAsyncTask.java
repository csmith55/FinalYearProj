package app.com.project.csmith.finalyearproject.AsyncTasks;

import android.os.AsyncTask;

import com.example.csmith.myapplication.backend.myApi.MyApi;
import com.facebook.model.GraphUser;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
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

    private LatLng usersLatLng;


    public GetLocationAsyncTask(List<GraphUser> graphUsers, ArrayList<FBFriendDetails> friendDetails, MainFragment mainFragment, LatLng latLng) {
        this.graphUsers = graphUsers;
        this.mainFragment = mainFragment;
        this.usersLatLng = latLng;

        this.friendDetails = friendDetails;
    }


    @Override
    protected Void doInBackground(Void... params) {
        ApiBuilder.buildApi(myApiService);

        for (int i = 0; i < graphUsers.size(); i++) {
            try {
                List<Double> list = myApiService.getLocation(graphUsers.get(i).getId()).execute().getData();
                friendDetails.add(new FBFriendDetails(graphUsers.get(i).getId(), graphUsers.get(i).getName(), new LatLng(list.get(0), list.get(1))));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (!friendDetails.isEmpty())
            new CalculateDistance(friendDetails, mainFragment, usersLatLng).rtreeQuery(9);

    }

}