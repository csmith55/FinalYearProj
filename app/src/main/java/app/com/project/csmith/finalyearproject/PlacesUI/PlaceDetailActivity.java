package app.com.project.csmith.finalyearproject.PlacesUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import app.com.project.csmith.finalyearproject.R;
import app.com.project.csmith.finalyearproject.QueryUI.SettingsActivity;


public class PlaceDetailActivity extends Activity {


    /**
     * Creates the window  UI for Place Detail
     * @param savedInstanceState state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceDetailsFragment())
                    .commit();
        }
    }

    /**
     * Sets the behavior of the menu options when selected
     * @param item selected
     * @return if the action wsa carried out
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

