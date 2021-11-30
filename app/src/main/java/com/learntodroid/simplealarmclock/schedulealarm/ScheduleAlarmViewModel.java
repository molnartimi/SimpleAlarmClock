package com.learntodroid.simplealarmclock.schedulealarm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.learntodroid.simplealarmclock.data.alarm.Alarm;
import com.learntodroid.simplealarmclock.data.alarm.AlarmRepository;

import java.util.Random;

public class ScheduleAlarmViewModel extends AndroidViewModel {
    private AlarmRepository alarmRepository;
    private boolean isNewAlarm;
    private Alarm alarm;

    public ScheduleAlarmViewModel(@NonNull Application application) {
        super(application);

        alarmRepository = new AlarmRepository(application);
    }

    public Alarm save(int hour, int minute, String title) {
        alarm.setHour(hour);
        alarm.setMinute(minute);
        alarm.setTitle(title);
        if (isNewAlarm) {
            alarmRepository.insert(alarm);
        } else {
            alarm.setStarted(true);
            alarmRepository.update(alarm);
        }
        return alarm;
    }

    public LiveData<Alarm> getAlarm(int alarmId) {
        return Transformations.map(alarmRepository.getAlarm(alarmId), alarm -> {
            this.isNewAlarm = alarm == null;
            this.alarm = alarm != null
                    ? alarm
                    : new Alarm(new Random().nextInt(Integer.MAX_VALUE), 7, 0);
            return this.alarm;
        });
    }
}
