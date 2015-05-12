package app.com.project.csmith.finalyearproject.Utilities;

import com.example.csmith.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

/**
 * Builds the root url for the API Backend
 */
public abstract class ApiBuilder {

    public static MyApi buildApi(MyApi myApiService) {

        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://finalyearproject40057321.appspot.com/_ah/api/");


            return builder.build();
        }
        return myApiService;
    }
}
