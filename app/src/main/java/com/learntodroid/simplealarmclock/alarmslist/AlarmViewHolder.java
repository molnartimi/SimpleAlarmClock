package com.learntodroid.simplealarmclock.alarmslist;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.learntodroid.simplealarmclock.data.alarm.Alarm;
import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.databinding.ItemAlarmBinding;

public class AlarmViewHolder extends RecyclerView.ViewHolder {
    private final ItemAlarmBinding binding;
    private final OnManageAlarmListener listener;

    public AlarmViewHolder(ItemAlarmBinding binding, OnManageAlarmListener listener) {
        super(binding.getRoot());
        this.listener = listener;
        this.binding = binding;
    }

    public void bind(Alarm alarm) {
        String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());

        binding.alarmItem.setOnClickListener(v -> listener.onEdit(alarm));

        binding.itemAlarmTime.setText(alarmText);
        binding.itemAlarmStarted.setChecked(alarm.isStarted());

        if (alarm.getTitle().length() != 0) {
            binding.itemAlarmTitle.setText(alarm.getTitle());
        } else {
            binding.itemAlarmTitle.setVisibility(View.GONE);
        }

        binding.itemAlarmRecurringDays.setText(alarm.isRecurring()
                ? alarm.forAllDay()
                    ? "Minden nap"
                    : "Néhány napon ismétlődő"
                : "Holnap");

        binding.itemAlarmStarted.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onToggle(alarm));

        //binding.itemAlarmDeleteButton.setOnClickListener(v -> listener.onDelete(alarm));
    }

    public void onViewRecycled() {
        binding.itemAlarmStarted.setOnCheckedChangeListener(null);
    }
}
