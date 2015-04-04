package app.com.project.csmith.finalyearproject.AsyncTasks;

import android.os.AsyncTask;

import com.example.csmith.myapplication.backend.myApi.MyApi;
import com.example.csmith.myapplication.backend.myApi.model.LocationDetails;
import com.facebook.model.GraphUser;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.com.project.csmith.finalyearproject.UIPermissions.MainFragment;

/**
 * Created by csmith on 22/02/15.
 */
public class GetAllLocationAsyncTask extends AsyncTask<Void, Void, Void> {
    private static MyApi myApiService = null;
    private List<GraphUser> graphUsers;
    private ArrayList<FBFriendDetails> allDetails;
    private MainFragment mainFragment;

    private LatLng usersLatLng;


    public GetAllLocationAsyncTask(List<GraphUser> graphUsers, ArrayList<FBFriendDetails> allDetails, MainFragment mainFragment, LatLng latLng) {
        this.graphUsers = graphUsers;
        this.mainFragment = mainFragment;
        this.usersLatLng = latLng;

        this.allDetails = allDetails;
    }


    @Override
    protected Void doInBackground(Void... params) {
        ApiBuilder.buildApi(myApiService);
        try {
            List<LocationDetails> list = myApiService.getAllLocations().execute().getData();
            for (LocationDetails details : list)
                allDetails.add(new FBFriendDetails(details.getId(), new LatLng(details.getLat(), details.getLng())));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (!allDetails.isEmpty())
            new CalculateDistance(allDetails, mainFragment, usersLatLng).rtreeQuery(5);

    }

}