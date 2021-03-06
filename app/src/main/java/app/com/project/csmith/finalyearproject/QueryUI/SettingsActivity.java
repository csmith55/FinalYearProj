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
package app.com.project.csmith.finalyearproject.QueryUI;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import app.com.project.csmith.finalyearproject.HomeUI.MainActivity;
import app.com.project.csmith.finalyearproject.R;


public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    /**
     * Creates the UI and variables to be displayed
     * @param savedInstanceState saved
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);

        Preference button = (Preference)findPreference(getString(R.string.aboutButton));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;
            }
        });


        //Binds the values inputted to the SharedPreferences
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_value_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_queryType_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_measurementsType_key)));
        final SwitchPreference switchPreference = (SwitchPreference) findPreference(getString(R.string.share_location));
        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference,
                                              Object newValue) {
                boolean switched = ((SwitchPreference) preference)
                        .isChecked();
                switchPreference.setChecked(!switched);


                return true;
            }

        });

    }

    /**
     * Returns user back to home screen on back pressed
     */
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    /**
     * binds SharedPreferences
     * @param preference
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);


        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {

            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {

            preference.setSummary(stringValue);
        }
        return true;
    }

}