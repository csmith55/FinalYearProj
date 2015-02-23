package app.com.project.csmith.finalyearproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import com.example.csmith.myapplication.backend.myApi.MyApi;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

class EndpointsAsyncTask extends AsyncTask<Pair<Context, LatLng>, Void, Void> {
    private static MyApi myApiService = null;
    private final String facebookId;
    private Context context;

    public EndpointsAsyncTask(String facebookId){
        this.facebookId = facebookId;
    }

    @Override
    protected Void doInBackground(Pair<Context, LatLng>... params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://finalyearproject40057321.appspot.com/_ah/api/");


            myApiService = builder.build();
        }

        context = params[0].first;


        try {

             myApiService.updateLocation(facebookId,params[0].second.latitude,params[0].second.longitude).execute();
        } catch (IOException e) {

        }
        return null;
    }

}
