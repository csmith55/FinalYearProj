package app.com.project.csmith.finalyearproject.LocationTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import com.example.csmith.myapplication.backend.myApi.MyApi;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.util.DateTime;

import java.io.IOException;

import app.com.project.csmith.finalyearproject.Utilities.ApiBuilder;

public class UpdateLocationAsyncTask extends AsyncTask<Pair<Context, LatLng>, Void, Void> {
    private static MyApi myApiService = null;
    private final String facebookId;

    public UpdateLocationAsyncTask(String facebookId) {
        this.facebookId = facebookId;
    }

    /**
     * Calls the Api Backend to update the location
     * Passes the id, lat and Lng plus the current date to the api
     * @param params current context and LatLng
     * @return void
     */
    @Override
    protected Void doInBackground(Pair<Context, LatLng>... params) {
        myApiService = ApiBuilder.buildApi(myApiService);

        try {
            myApiService.updateLocation(facebookId, params[0].second.latitude, params[0].second.longitude, new DateTime(System.currentTimeMillis())).execute();
        } catch (IOException e) {

        }
        return null;
    }

}
