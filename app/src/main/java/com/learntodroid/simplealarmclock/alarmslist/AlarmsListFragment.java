package com.learntodroid.simplealarmclock.alarmslist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.learntodroid.simplealarmclock.activities.ScheduleAlarmActivity;
import com.learntodroid.simplealarmclock.data.alarm.Alarm;
import com.learntodroid.simplealarmclock.databinding.FragmentListalarmsBinding;

public class AlarmsListFragment extends Fragment implements OnManageAlarmListener {
    private FragmentListalarmsBinding binding;

    private AlarmRecyclerViewAdapter alarmRecyclerViewAdapter;
    private AlarmsListViewModel alarmsListViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(this);
        alarmsListViewModel = ViewModelProviders.of(this).get(AlarmsListViewModel.class);
        alarmsListViewModel.getAlarmsLiveData().observe(this, alarms -> {
            if (alarms != null) {
                alarmRecyclerViewAdapter.setAlarms(alarms);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListalarmsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.fragmentListalarmsRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fragmentListalarmsRecylerView.setAdapter(alarmRecyclerViewAdapter);

        binding.fragmentListalarmsAddAlarm.setOnClickListener(v -> startScheduleAlarmActivity());

        return view;
    }

    private void startScheduleAlarmActivity() {
        startActivity(new Intent(getContext(), ScheduleAlarmActivity.class));
    }

    private void startScheduleAlarmActivity(Alarm alarm) {
        Intent intent = new Intent(getContext(), ScheduleAlarmActivity.class);
        intent.putExtra("alarmId", alarm.getAlarmId());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onToggle(Alarm alarm) {
        if (alarm.isStarted()) {
            alarm.cancelAlarm(requireContext());
            alarmsListViewModel.update(alarm);
        } else {
            alarm.schedule(requireContext());
            alarmsListViewModel.update(alarm);
        }
    }

    @Override
    public void onDelete(Alarm alarm) {
        alarm.cancelAlarm(requireContext());
        alarmsListViewModel.delete(alarm);
    }

    @Override
    public void onEdit(Alarm alarm) {
        startScheduleAlarmActivity(alarm);
    }
}