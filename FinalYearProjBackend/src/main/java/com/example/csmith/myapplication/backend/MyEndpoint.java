/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.csmith.myapplication.backend;


import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(name = "myApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.myapplication.csmith.example.com", ownerName = "backend.myapplication.csmith.example.com", packagePath = ""))
public class MyEndpoint {
    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();


    @ApiMethod(name = "updateLocation")
    public void updateLocation(@Named("fbId") String facebookId, @Named("Lng") Double lat, @Named("Lat") Double lng) {

        Entity newLocation = new Entity("Location", facebookId);
        newLocation.setProperty("lat", lat);
        newLocation.setProperty("lng", lng);

        datastoreService.put(newLocation);

    }

    @ApiMethod(name = "getLocation")
    public MyBean getLocation(@Named("facebookId") String facebookId) throws EntityNotFoundException {

        Key key = KeyFactory.createKey("Location", facebookId);

        MyBean response = new MyBean();

        Entity entity = datastoreService.get(key);

        double[] latLng = new double[2];
        latLng[0] = (double) entity.getProperty("lat");
        latLng[1] = (double) entity.getProperty("lng");
        response.setData(latLng);
        return response;

    }

    @ApiMethod(name = "getAllLocations")
    public MyRTreeBean getAllLocations() throws EntityNotFoundException {


        MyRTreeBean response = new MyRTreeBean();

        Query query = new Query("Location");
        PreparedQuery preparedQuery = datastoreService.prepare(query);

        for (Entity entity : preparedQuery.asIterable()) {
            response.setData(new LocationDetails(entity.getKey().getName(), (double) entity.getProperty("lat"), (double) entity.getProperty("lng")));
        }

        return response;

    }


}
