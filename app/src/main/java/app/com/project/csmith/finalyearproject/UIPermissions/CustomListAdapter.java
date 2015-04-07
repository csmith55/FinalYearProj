package app.com.project.csmith.finalyearproject.UIPermissions;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import java.util.ArrayList;

import app.com.project.csmith.finalyearproject.AsyncTasks.FBFriendDetails;
import app.com.project.csmith.finalyearproject.R;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> addresses;
    private final ArrayList<FBFriendDetails> profilePictureViews;

    public CustomListAdapter(Activity context, ArrayList<String> addresses, ArrayList<FBFriendDetails> profilePictureViews) {
        super(context, R.layout.results_list, addresses);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.addresses =addresses;
        this.profilePictureViews =profilePictureViews;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.results_list, null,true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ProfilePictureView imageView = (ProfilePictureView) rowView.findViewById(R.id.icon);

        for(int i = 0; i < addresses.size(); i++) {
             txtTitle = (TextView) rowView.findViewById(R.id.item);
             imageView = (ProfilePictureView) rowView.findViewById(R.id.icon);
            imageView.setProfileId(profilePictureViews.get(position).getID());
            imageView.setCropped(true);
            TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

            txtTitle.setText(profilePictureViews.get(position).getName());

            extratxt.setText("Location: " + addresses.get(position) + "\nDistance: " + profilePictureViews.get(position).getDistanceText());
        }
        return rowView;

    }


    public String getName(int position) {
       return profilePictureViews.get(position).getName();
    }

    public String getLocation(int position) {
        return addresses.get(position);
    }
}