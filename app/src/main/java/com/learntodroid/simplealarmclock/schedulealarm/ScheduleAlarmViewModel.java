package com.learntodroid.simplealarmclock.schedulealarm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.learntodroid.simplealarmclock.data.alarm.Alarm;
import com.learntodroid.simplealarmclock.data.alarm.AlarmRepository;

public class ScheduleAlarmViewModel extends AndroidViewModel {
    private AlarmRepository alarmRepository;

    public ScheduleAlarmViewModel(@NonNull Application application) {
        super(application);

        alarmRepository = new AlarmRepository(application);
    }

    public void insert(Alarm alarm) {
        alarmRepository.insert(alarm);
    }
}
