package com.learntodroid.simplealarmclock.alarmslist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learntodroid.simplealarmclock.data.alarm.Alarm;
import com.learntodroid.simplealarmclock.databinding.ItemAlarmBinding;
import com.learntodroid.simplealarmclock.common.ListItemSelectionHandler;

import java.util.ArrayList;
import java.util.List;

public class AlarmRecyclerViewAdapter extends RecyclerView.Adapter<AlarmViewHolder> {
    private List<Alarm> alarms;
    private final OnManageAlarmListener listener;
    private final ListItemSelectionHandler<Alarm> selectionHandler;

    public AlarmRecyclerViewAdapter(OnManageAlarmListener listener, ListItemSelectionHandler<Alarm> selectionHandler) {
        this.alarms = new ArrayList<>();
        this.listener = listener;
        this.selectionHandler = selectionHandler;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAlarmBinding binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AlarmViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        holder.bind(alarm, selectionHandler);
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull AlarmViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }
}

