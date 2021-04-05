package com.learntodroid.simplealarmclock.service;

import com.learntodroid.simplealarmclock.emergency.OnResponseTimerFiredListener;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class EmergencyMessageSendingTimerService {
    private static Timer responseTimer;
    private static Set<OnResponseTimerFiredListener> listeners;

    public static void startTimer(int timeout) {
        if (responseTimer == null) {
            responseTimer = new Timer();
        }
        responseTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                callListeners();
            }
        }, timeout);
    }

    public static void stopTimer() {
        if (responseTimer != null) {
            responseTimer.cancel();
            responseTimer = null;
            listeners.clear();
        }
    }

    public static void addTimeoutListener(OnResponseTimerFiredListener listener) {
        if (listeners == null) {
            listeners = new HashSet<>();
        }
        listeners.add(listener);
    }

    private static void callListeners() {
        for (OnResponseTimerFiredListener listener: listeners) {
            listener.onTimeout();
        }
    }
}
