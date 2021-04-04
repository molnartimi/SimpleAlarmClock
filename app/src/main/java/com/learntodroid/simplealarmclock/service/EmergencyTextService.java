package com.learntodroid.simplealarmclock.service;

import android.content.Context;
import android.content.SharedPreferences;

public class EmergencyTextService {
    private static final String PREF_FILE_KEY = "simple_alarm_clock_preferences";
    private static final String TEXT_KEY = "emergency_message_text";
    private static final String DEFAULT_MESSAGE =
            "[TESZT SMS] SEGÍTS! Ez egy automatikusan küldött SMS a Vészjelző Ébresztő alkalmazásból, " +
                    "mert nem reagáltam a beállított ébresztőre. " +
                    "Kérlek nézz rám, lehet, hogy rosszul vagyok!";

    private SharedPreferences sharedPref;

    public EmergencyTextService(Context context) {
        sharedPref = context.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE);
    }

    public String getMessageText() {
        return sharedPref.getString(TEXT_KEY, DEFAULT_MESSAGE).trim();
    }

    public void updateMessageText(String message) {
        if (message == null) {
            // TODO LOG warning
            return;
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TEXT_KEY, message);
        editor.apply();
    }
}
