package com.learntodroid.simplealarmclock.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public abstract class ValueHolderBase {

    private static final String PREF_FILE_KEY = "simple_alarm_clock_preferences";

    private SharedPreferences sharedPref;

    protected ValueHolderBase(Context context) {
        sharedPref = context.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE);
    }

    protected String getValue() {
        return sharedPref.getString(getKey(), getDefaultValue()).trim();
    }

    protected void updateValue(String value) {
        if (value == null) {
            Log.w(getLoggingTag(), "Null was passed to save to system preference, update is skipped.");
            return;
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getKey(), value);
        editor.apply();
    }

    protected abstract String getKey();
    protected abstract String getDefaultValue();
    protected abstract String getLoggingTag();

}
