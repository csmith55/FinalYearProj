package app.com.project.csmith.finalyearproject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by csmith on 05/03/15.
 */
abstract class GeocoderUtil {

    public static List<Address> convertLatLngToAddress(LatLng latLng, Context context){
        Geocoder geocoder;

        geocoder = new Geocoder(context, Locale.getDefault());
        if (latLng != null) {
            try {
                return geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
