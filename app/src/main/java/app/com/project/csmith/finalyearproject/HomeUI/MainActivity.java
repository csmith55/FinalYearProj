/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.com.project.csmith.finalyearproject.HomeUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import app.com.project.csmith.finalyearproject.R;


public class MainActivity extends FragmentActivity {

    private static final int SETTINGS = 2;
    private static final int FRAGMENT_COUNT = SETTINGS + 1;
    private final Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    private static final int SPLASH = 0;
    private static final int SELECTION = 1;

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final Session.StatusCallback callback =
            new Session.StatusCallback() {
                @Override
                public void call(Session session,
                                 SessionState state, Exception exception) {
                    onSessionStateChange(state);
                }
            };
    private MenuItem settings;
    private boolean isResumed = false;
    private UiLifecycleHelper uiHelper;


    /**
     * Initialises the view and the values within the UI
     * @param savedInstanceState if the application is killed/reopened
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getKeyHashForFacebook();
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setFragments();
    }

    /**
     * Creates the fragments required for log in and showing the home screen
     */

    private void setFragments() {
        FragmentManager fm = getSupportFragmentManager();
        getFragments()[SPLASH] = fm.findFragmentById(R.id.splashFragment);
        getFragments()[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
        getFragments()[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);


        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < getFragments().length; i++) {
            transaction.hide(getFragments()[i]);
        }
        transaction.commit();
    }

    /**
     * If the user selects options from the menu display the fragment
     * @param item menuitem that is pressed
     * @return true if the value is pressed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(settings)) {
            showFragment(SETTINGS, true);
            return true;
        }
        return false;
    }

    /**
     * Determines which fragment is required to be displayed
     * Based upon the facebook developers example
     * https://developers.facebook.com/docs/android
     * @param fragmentIndex the index of the fragment to be displayed
     * @param addToBackStack if the fragment should be added to the stack
     */
    private void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < getFragments().length; i++) {
            if (i == fragmentIndex) {
                transaction.show(getFragments()[i]);
            } else {
                transaction.hide(getFragments()[i]);
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    /**
     *Taken from Facebook developers example
     * https://developers.facebook.com/docs/android
     * How to determine which fragment is active
     * @param state current state of the application
     */
    private void onSessionStateChange(SessionState state) {
        // Only make changes if the activity is visible
        if (isResumed) {
            FragmentManager manager = getSupportFragmentManager();
            // Get the number of entries in the back stack
            int backStackSize = manager.getBackStackEntryCount();
            // Clear the back stack
            for (int i = 0; i < backStackSize; i++) {
                manager.popBackStack();
            }
            showAppropiateFragment(state);
        }
    }

    /**
     * Shows the correct fragment
     * @param state current facebook session status
     */
    private void showAppropiateFragment(SessionState state) {
        if (state.isOpened()) {
            // If the session state is open:
            // Show the authenticated fragment
            showFragment(SELECTION, false);
        } else if (state.isClosed()) {
            // If the session state is closed:
            // Show the login fragment
            showFragment(SPLASH, false);
        }
    }

    /**
     *  Check fragment state once app has been reopened
     */
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Session session = Session.getActiveSession();

        if (session != null && session.isOpened()) {
            // if the session is already open,
            // try to show the selection fragment
            showFragment(SELECTION, false);
        } else {
            // otherwise present the splash screen
            // and ask the person to login.
            showFragment(SPLASH, false);
        }
    }

    /**
     * Log when the application has been resumed
     */
    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        uiHelper.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);

    }

    /**
     * Log that the application has been paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
        uiHelper.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    /**
     * Set the uiHelper with the current request code
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Destroys the UI
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    /**
     * Saves the current instance state
     * @param outState The bundle to save the state in
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    /**
     *
     * @return the fragment array
     */
    public Fragment[] getFragments() {
        return fragments;
    }

}
