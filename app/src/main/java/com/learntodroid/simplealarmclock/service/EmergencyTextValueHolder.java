package com.learntodroid.simplealarmclock.service;

import android.content.Context;

import com.learntodroid.simplealarmclock.R;

public class EmergencyTextValueHolder extends ValueHolderBase {

    private final String defaultValue;

    public EmergencyTextValueHolder(Context context) {
        super(context);
        defaultValue = context.getString(R.string.default_emergency_message_text);
    }

    public String getMessageText() {
        return getValue();
    }

    public void updateMessageText(String message) {
        updateValue(message);
    }

    @Override
    protected String getKey() {
        return "emergency_message_text";
    }

    @Override
    protected String getDefaultValue() {
        return defaultValue;
    }

    @Override
    protected String getLoggingTag() {
        return "EmergencyTextValueHolder";
    }
}
