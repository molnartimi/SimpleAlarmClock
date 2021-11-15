package com.learntodroid.simplealarmclock.settings.message;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        binding.savedMessage.setOnClickListener(v -> setMessageTextEditable());
        binding.saveBtn.setOnClickListener(v -> saveEditedText());
        binding.cancleBtn.setOnClickListener(v -> cancelTextEditing());

        return view;
    }

    private void saveEditedText() {
        String newMessageText = binding.edittextMessage.getText().toString();
        emergencyTextService.updateMessageText(newMessageText);
        binding.savedMessage.setText(newMessageText);
        toggleMessageTextView(false);
    }

    private void cancelTextEditing() {
        toggleMessageTextView(false);
    }

    private void setMessageTextEditable() {
        binding.edittextMessage.setText(emergencyTextService.getMessageText());
        toggleMessageTextView(true);
    }

    private void toggleMessageTextView(boolean editable) {
        int edit = editable ? View.VISIBLE : View.GONE;
        int view = editable ? View.GONE : View.VISIBLE;

        binding.savedMessage.setVisibility(view);

        binding.edittextMessage.setVisibility(edit);
        binding.saveBtn.setVisibility(edit);
        binding.cancleBtn.setVisibility(edit);
        if (!editable) {
            hideKeyboard();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.edittextMessage.getWindowToken(), 0);
    }
}
