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

        binding.fragmentCreatealarmRecurring.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.fragmentCreatealarmRecurringOptions.setVisibility(View.VISIBLE);
            } else {
                binding.fragmentCreatealarmRecurringOptions.setVisibility(View.GONE);
            }
        });

        binding.fragmentCreatealarmScheduleAlarm.setOnClickListener(v -> {
            scheduleAlarm();
            getActivity().finish();
        });

        binding.fragmentCreatealarmCancelButton.setOnClickListener(v -> getActivity().finish());

        return view;
    }

    private void scheduleAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = new Alarm(
                alarmId,
                TimePickerUtil.getTimePickerHour(binding.fragmentCreatealarmTimePicker),
                TimePickerUtil.getTimePickerMinute(binding.fragmentCreatealarmTimePicker),
                binding.fragmentCreatealarmTitle.getText().toString(),
                System.currentTimeMillis(),
                true,
                binding.fragmentCreatealarmRecurring.isChecked(),
                binding.fragmentCreatealarmCheckMon.isChecked(),
                binding.fragmentCreatealarmCheckTue.isChecked(),
                binding.fragmentCreatealarmCheckWed.isChecked(),
                binding.fragmentCreatealarmCheckThu.isChecked(),
                binding.fragmentCreatealarmCheckFri.isChecked(),
                binding.fragmentCreatealarmCheckSat.isChecked(),
                binding.fragmentCreatealarmCheckSun.isChecked()
        );

        createAlarmViewModel.insert(alarm);

        alarm.schedule(requireContext());
    }
}
