package com.learntodroid.simplealarmclock.settings.timer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.databinding.FragmentTimerBinding;
import com.learntodroid.simplealarmclock.service.SavedTimeoutValueHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimerFragment extends Fragment {

    private SavedTimeoutValueHolder timeoutValueHolder;
    private FragmentTimerBinding binding;
    private String MINUTE_STRING;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeoutValueHolder = new SavedTimeoutValueHolder(requireContext());
        MINUTE_STRING = getResources().getString(R.string.minute);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTimerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        updateText();
        binding.savedTimer.setOnClickListener(v -> showEditDialog());

        return view;
    }

    private void showEditDialog() {
        List<Integer> values = Arrays.asList(1, 2, 3, 5, 10);
        ArrayList<String> stringValues = new ArrayList<>();
        int selectedOne = values.indexOf(timeoutValueHolder.getTimeoutInt());

        for (int i = 0; i < values.size(); i++) {
            stringValues.add(values.get(i) + " " + MINUTE_STRING);
        }

        AlertDialog alert = new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.emergency_timeout))
                .setSingleChoiceItems(stringValues.toArray(new String[0]), selectedOne, (dialog, selectedIdx) -> {
                    timeoutValueHolder.setTimeoutString(String.valueOf(values.get(selectedIdx)));
                    updateText();
                    dialog.dismiss();
                })
                .create();
        alert.show();
    }

    private void updateText() {
        binding.savedTimer.setText(String.format("%s %s", timeoutValueHolder.getTimeoutString(), MINUTE_STRING));
    }

}
