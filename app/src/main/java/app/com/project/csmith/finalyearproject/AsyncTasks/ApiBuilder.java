package app.com.project.csmith.finalyearproject.AsyncTasks;

import com.example.csmith.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

/**
 * Created by csmith on 05/04/15.
 */
 abstract class ApiBuilder {

    protected static MyApi buildApi(MyApi myApiService) {

        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://finalyearproject40057321.appspot.com/_ah/api/");


            return builder.build();
        }
        return myApiService;
    }
}
