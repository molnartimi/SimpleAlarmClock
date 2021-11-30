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

        int alarmId = getActivity().getIntent().getIntExtra("alarmId", -1);
        createAlarmViewModel.getAlarm(alarmId).observe(getViewLifecycleOwner(), alarm -> {
            TimePickerUtil.setTimePickerHour(binding.timePicker, alarm.getHour());
            TimePickerUtil.setTimePickerMinute(binding.timePicker, alarm.getMinute());
            binding.newAlarmName.setText(alarm.getTitle());
            binding.isNewAlarmRecurring.setChecked(alarm.isRecurring());
            binding.weekDaysHolder.setVisibility(alarm.isRecurring() ? View.VISIBLE : View.GONE);
            binding.isNewAlarmRecurring.setOnCheckedChangeListener((v, isChecked) -> {
                alarm.setRecurring(isChecked);
                binding.weekDaysHolder.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                bindWeekDays(alarm);
            });
            bindWeekDays(alarm);
            binding.onMon.setOnCheckedChangeListener((v, isChecked) -> alarm.setMonday(isChecked));
            binding.onTue.setOnCheckedChangeListener((v, isChecked) -> alarm.setTuesday(isChecked));
            binding.onWed.setOnCheckedChangeListener((v, isChecked) -> alarm.setWednesday(isChecked));
            binding.onThu.setOnCheckedChangeListener((v, isChecked) -> alarm.setThursday(isChecked));
            binding.onFri.setOnCheckedChangeListener((v, isChecked) -> alarm.setFriday(isChecked));
            binding.onSat.setOnCheckedChangeListener((v, isChecked) -> alarm.setSaturday(isChecked));
            binding.onSun.setOnCheckedChangeListener((v, isChecked) -> alarm.setSunday(isChecked));
        });

        binding.timePicker.setIs24HourView(true);

        binding.saveScheduleAlarmBtn.setOnClickListener(v -> {
            scheduleAlarm();
            getActivity().finish();
        });

        binding.cancelScheduleAlarmBtn.setOnClickListener(v -> getActivity().finish());

        return view;
    }

    private void scheduleAlarm() {
        Alarm alarm = createAlarmViewModel.save(TimePickerUtil.getTimePickerHour(binding.timePicker),
                TimePickerUtil.getTimePickerMinute(binding.timePicker),
                binding.newAlarmName.getText().toString());
        alarm.schedule(requireContext());
    }

    private void bindWeekDays(Alarm alarm) {
        binding.onMon.setChecked(alarm.isMonday());
        binding.onTue.setChecked(alarm.isTuesday());
        binding.onWed.setChecked(alarm.isWednesday());
        binding.onThu.setChecked(alarm.isThursday());
        binding.onFri.setChecked(alarm.isFriday());
        binding.onSat.setChecked(alarm.isSaturday());
        binding.onSun.setChecked(alarm.isSunday());
    }
}
