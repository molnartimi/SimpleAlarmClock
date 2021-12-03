package com.learntodroid.simplealarmclock.alarmslist;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.learntodroid.simplealarmclock.common.ListItemSelectionHandler;
import com.learntodroid.simplealarmclock.data.alarm.Alarm;
import com.learntodroid.simplealarmclock.databinding.ItemAlarmBinding;

import java.util.Observable;
import java.util.Observer;

public class AlarmViewHolder extends RecyclerView.ViewHolder implements Observer {
    private final ItemAlarmBinding binding;
    private final OnManageAlarmListener listener;

    public AlarmViewHolder(ItemAlarmBinding binding, OnManageAlarmListener listener) {
        super(binding.getRoot());
        this.listener = listener;
        this.binding = binding;
    }

    public void bind(Alarm alarm, ListItemSelectionHandler<Alarm> selectionHandler) {
        String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());

        binding.alarmItem.setOnClickListener(v -> {
            if (!selectionHandler.isInSelectableMode()) {
                listener.onEdit(alarm);
            } else {
                binding.alarmCheckbox.setChecked(!binding.alarmCheckbox.isChecked());
            }
        });
        binding.alarmItem.setOnLongClickListener(v -> {
            selectionHandler.setSelectable(true);
            binding.alarmCheckbox.setChecked(true);
            return true;
        });

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

        binding.alarmCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (selectionHandler.isInSelectableMode()) {
                if (isChecked) {
                    selectionHandler.select(alarm);
                } else {
                    selectionHandler.deselect(alarm);
                }
            }
        });

        selectionHandler.addObserver(this);
    }

    public void onViewRecycled() {
        binding.itemAlarmStarted.setOnCheckedChangeListener(null);
    }

    @Override
    public void update(Observable observable, Object o) {
        Boolean selectableMode = (Boolean) o;
        binding.alarmCheckbox.setVisibility(selectableMode ? View.VISIBLE : View.GONE);
        binding.itemAlarmStarted.setVisibility(selectableMode ? View.GONE : View.VISIBLE);
        if (!selectableMode) {
            binding.alarmCheckbox.setChecked(false);
        }
    }
}
