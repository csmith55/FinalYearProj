package app.com.project.csmith.finalyearproject.PlacesUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import app.com.project.csmith.finalyearproject.R;
import app.com.project.csmith.finalyearproject.QueryUI.SettingsActivity;

public class PlacesActivity extends Activity {

    /**
     * Creates the UI and variables to be displayed
     * @param savedInstanceState saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlacesFragment())
                    .commit();
        }
    }

    /**
     * Creates the menu action bar for the UI
     * @param menu menu to create
     * @return if the action was completed
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    /**
     * Determines the item selected from the menu and starts the next activity
     * @param item selected
     * @return if the action was completed
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
