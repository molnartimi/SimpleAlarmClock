package com.learntodroid.simplealarmclock.schedulealarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.learntodroid.simplealarmclock.data.alarm.Alarm;
import com.learntodroid.simplealarmclock.databinding.FragmentSchedulealarmBinding;

import java.util.Random;

public class ScheduleAlarmFragment extends Fragment {
    private FragmentSchedulealarmBinding binding;
    private ScheduleAlarmViewModel createAlarmViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createAlarmViewModel = ViewModelProviders.of(this).get(ScheduleAlarmViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSchedulealarmBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.timePicker.setIs24HourView(true);
        TimePickerUtil.setTimePickerHour(binding.timePicker, 7);
        TimePickerUtil.setTimePickerMinute(binding.timePicker, 0);

        binding.isNewAlarmRecurring.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.weekDaysHolder.setVisibility(View.VISIBLE);
            } else {
                binding.weekDaysHolder.setVisibility(View.GONE);
            }
        });

        binding.saveScheduleAlarmBtn.setOnClickListener(v -> {
            scheduleAlarm();
            getActivity().finish();
        });

        binding.cancelScheduleAlarmBtn.setOnClickListener(v -> getActivity().finish());

        return view;
    }

    private void scheduleAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = new Alarm(
                alarmId,
                TimePickerUtil.getTimePickerHour(binding.timePicker),
                TimePickerUtil.getTimePickerMinute(binding.timePicker),
                binding.newAlarmName.getText().toString(),
                System.currentTimeMillis(),
                true,
                binding.isNewAlarmRecurring.isChecked(),
                binding.onMon.isChecked(),
                binding.onTue.isChecked(),
                binding.onWed.isChecked(),
                binding.onThu.isChecked(),
                binding.onFri.isChecked(),
                binding.onSat.isChecked(),
                binding.onSun.isChecked()
        );

        createAlarmViewModel.insert(alarm);

        alarm.schedule(requireContext());
    }
}
