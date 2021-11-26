package com.learntodroid.simplealarmclock.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.learntodroid.simplealarmclock.databinding.ActivityCreatealarmBinding;

public class ScheduleAlarmActivity extends AppCompatActivity {
    private ActivityCreatealarmBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatealarmBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

}
