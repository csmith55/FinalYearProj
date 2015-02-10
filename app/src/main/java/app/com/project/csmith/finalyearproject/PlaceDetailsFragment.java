package app.com.project.csmith.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by csmith on 10/02/15.
 */
public class PlaceDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_place_details, container, false);

        Intent intent = getActivity().getIntent();
        if(intent.hasExtra("Place")){
            Places place = null;
            ((TextView) rootView.findViewById(R.id.placeName)).setText(place.getName());
            ((TextView) rootView.findViewById(R.id.placeWebsite)).setText(place.getWebsite());
            ((TextView) rootView.findViewById(R.id.placeNumber)).setText(place.getPhoneNumber());
            //((ImageView) rootView.findViewById(R.id.placeIcon)).setImageResource(place.getImage());

        }
        return rootView;
    }
}
