package app.com.project.csmith.finalyearproject.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import com.example.csmith.myapplication.backend.myApi.MyApi;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.util.DateTime;

import java.io.IOException;

public class UpdateLocationAsyncTask extends AsyncTask<Pair<Context, LatLng>, Void, Void> {
    private static MyApi myApiService = null;
    private final String facebookId;

    public UpdateLocationAsyncTask(String facebookId) {
        this.facebookId = facebookId;
    }

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
