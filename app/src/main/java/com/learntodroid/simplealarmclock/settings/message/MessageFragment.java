package com.learntodroid.simplealarmclock.settings.message;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.databinding.FragmentMessageBinding;
import com.learntodroid.simplealarmclock.service.EmergencyTextValueHolder;

public class MessageFragment extends Fragment {

    FragmentMessageBinding binding;
    private EmergencyTextValueHolder emergencyTextService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emergencyTextService = new EmergencyTextValueHolder(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.savedMessage.setText(emergencyTextService.getMessageText());
        binding.savedMessage.setOnClickListener(v -> showEditDialog());

        return view;
    }

    private void showEditDialog() {
        final EditText editText = new EditText(getContext());
        editText.setText(emergencyTextService.getMessageText());

        FrameLayout container = new FrameLayout(getContext());
        container.addView(editText);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.setMargins(margin, 0, margin, 0);
        editText.setLayoutParams(params);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.edit_message_popup_title)
                .setView(container)
                .setPositiveButton(R.string.save, (d, w) -> saveMessage(String.valueOf(editText.getText())))
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();
    }

    private void saveMessage(String newMessage) {
        emergencyTextService.updateMessageText(newMessage);
        binding.savedMessage.setText(newMessage);
    }
}
