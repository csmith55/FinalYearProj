package app.com.project.csmith.finalyearproject.QueryUI;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import app.com.project.csmith.finalyearproject.R;

/**
 * Creates the about activity UI for the application
 */
public class AboutActivity extends FragmentActivity{
    /**
     * Creates the UI and variables to be displayed
     * @param savedInstanceState saved
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutlayout);
    }
}
