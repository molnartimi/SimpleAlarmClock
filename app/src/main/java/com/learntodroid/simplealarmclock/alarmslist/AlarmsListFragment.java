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
import com.learntodroid.simplealarmclock.common.ListItemSelectionHandler;
import com.learntodroid.simplealarmclock.data.alarm.Alarm;
import com.learntodroid.simplealarmclock.databinding.FragmentListalarmsBinding;

import java.util.Observable;
import java.util.Observer;

public class AlarmsListFragment extends Fragment implements OnManageAlarmListener, Observer {
    private FragmentListalarmsBinding binding;

    private AlarmRecyclerViewAdapter alarmRecyclerViewAdapter;
    private AlarmsListViewModel alarmsListViewModel;
    private ListItemSelectionHandler<Alarm> selectionHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectionHandler = new ListItemSelectionHandler<>();
        selectionHandler.addObserver(this);
        alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(this, selectionHandler);
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

        binding.createAlarmBtn.setOnClickListener(v -> startScheduleAlarmActivity());
        binding.removeSelectedAlarmsBtn.setOnClickListener(v -> {
            selectionHandler.setSelectable(false);
            deleteSelectedAlarms();
            selectionHandler.clear();
        });

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

    public void deleteSelectedAlarms() {
        for (Alarm alarm: selectionHandler.getSelectedItems()) {
            alarm.cancelAlarm(requireContext());
            alarmsListViewModel.delete(alarm);
        }
    }

    @Override
    public void onEdit(Alarm alarm) {
        startScheduleAlarmActivity(alarm);
    }

    @Override
    public void update(Observable observable, Object o) {
        Boolean selectableMode = (Boolean) o;
        binding.removeSelectedAlarmsBtn.setVisibility(selectableMode ? View.VISIBLE : View.GONE);
        binding.createAlarmBtn.setVisibility(selectableMode ? View.GONE : View.VISIBLE);
    }
}