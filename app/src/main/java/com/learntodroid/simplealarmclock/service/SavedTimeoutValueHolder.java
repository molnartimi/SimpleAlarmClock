package com.learntodroid.simplealarmclock.service;

import android.content.Context;

public class SavedTimeoutValueHolder extends ValueHolderBase {

    public SavedTimeoutValueHolder(Context context) {
        super(context);
    }

    public String getTimeoutString() {
        return getValue();
    }

    public int getTimeoutInMilisec() {
        return Integer.parseInt(getValue()) * 60 * 1000;
    }

    public void setTimeoutString(String min) {
        updateValue(min);
    }

    @Override
    protected String getKey() {
        return "emergency_message_timeout_in_min";
    }

    @Override
    protected String getDefaultValue() {
        return "1";
    }

    @Override
    protected String getLoggingTag() {
        return "SavedTimeoutValueHolder";
    }
}
