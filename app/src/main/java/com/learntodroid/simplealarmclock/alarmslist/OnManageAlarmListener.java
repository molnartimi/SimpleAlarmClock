package com.learntodroid.simplealarmclock.alarmslist;

import com.learntodroid.simplealarmclock.data.Alarm;

public interface OnManageAlarmListener {
    void onToggle(Alarm alarm);
    void onDelete(Alarm alarm);
}
