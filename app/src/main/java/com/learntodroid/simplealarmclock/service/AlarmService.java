package com.learntodroid.simplealarmclock.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.activities.RingActivity;

import static com.learntodroid.simplealarmclock.application.App.CHANNEL_ID;
import static com.learntodroid.simplealarmclock.broadcastreceiver.AlarmBroadcastReceiver.TITLE;

public class AlarmService extends Service {
    private static final String TAG = "AlarmService";

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private Vibrator vibrator;

    private int originalMediaVolume;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setLooping(true);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, RingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        String alarmTitle = String.format("%s Alarm", intent.getStringExtra(TITLE));

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText("Vészjelző Ébresztő")
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        setMediaVolume(true);
        mediaPlayer.start();

        long[] pattern = { 0, 100, 1000 };
        vibrator.vibrate(pattern, 0);

        startForeground(1, notification);

        return START_STICKY;
    }

    private void setMediaVolume(boolean starting) {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int newVolume;
        if (starting) {
            originalMediaVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            newVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            Log.i(TAG, String.format("Media stream volume set from %d to %d for Vészjelző Ébresztő", originalMediaVolume, newVolume));
        } else {
            newVolume = originalMediaVolume;
            Log.i(TAG, String.format("Media stream volume set back to original %d after Vészjelző Ébresztő alarm finished", originalMediaVolume));
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
        mediaPlayer.setVolume(100, 100);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
        setMediaVolume(false);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
