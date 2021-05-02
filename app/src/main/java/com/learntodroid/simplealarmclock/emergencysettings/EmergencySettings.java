package com.learntodroid.simplealarmclock.emergencysettings;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.learntodroid.simplealarmclock.R;

public class EmergencySettings extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.emergency_preferences, rootKey);

        Preference contactListPreference = findPreference("contact_list");

        if (contactListPreference != null) {
            contactListPreference.setSummaryProvider(preference -> "Nincs megadva értesítendő személy!");
        }

        contactListPreference.setOnPreferenceClickListener(preference -> false);
    }
}
