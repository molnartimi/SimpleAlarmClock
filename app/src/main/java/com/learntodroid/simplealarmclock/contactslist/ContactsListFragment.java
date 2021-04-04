package com.learntodroid.simplealarmclock.contactslist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.data.contact.Contact;
import com.learntodroid.simplealarmclock.service.EmergencyTextValueHolder;
import com.learntodroid.simplealarmclock.service.SavedTimeoutValueHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsListFragment extends Fragment implements OnManageContactListener {
    private static final int RESULT_PICK_CONTACT = 1;

    private ContactRecyclerViewAdapter contactRecyclerViewAdapter;
    private ContactsListViewModel contactsListViewModel;
    private EmergencyTextValueHolder emergencyTextService;
    private SavedTimeoutValueHolder timeoutValueHolder;
    @BindView(R.id.fragment_listcontacts_recylerView) RecyclerView contactsRecyclerView;
    @BindView(R.id.fragment_listcontacts_addContact) Button addContact;
    @BindView(R.id.fragment_listcontacts_editText_button) Button editTextButton;
    @BindView(R.id.fragment_listcontacts_saveText_button) Button saveTextButton;
    @BindView(R.id.fragment_listcontacts_editTextCancel_button) Button cancelEditTextButton;
    @BindView(R.id.fragment_listcontacts_messageText) TextView messageText;
    @BindView(R.id.fragment_listcontacts_messageTextEdit) EditText messageTextEdit;
    @BindView(R.id.fragment_listcontacts_timeout) EditText timeoutTextEdit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactRecyclerViewAdapter = new ContactRecyclerViewAdapter(this);
        contactsListViewModel = ViewModelProviders.of(this).get(ContactsListViewModel.class);
        contactsListViewModel.getContactsLiveData().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                if (contacts != null) {
                    contactRecyclerViewAdapter.setContacts(contacts);
                }
            }
        });

        emergencyTextService = new EmergencyTextValueHolder(requireContext());
        timeoutValueHolder = new SavedTimeoutValueHolder(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listcontacts, container, false);
        ButterKnife.bind(this, view);

        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactsRecyclerView.setAdapter(contactRecyclerViewAdapter);

        messageText.setText(emergencyTextService.getMessageText());

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSystemContactList();
            }
        });

        editTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMessageTextEditable();
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditedText();
            }
        });

        cancelEditTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTextEditing();
            }
        });

        timeoutTextEdit.setText(timeoutValueHolder.getTimeoutString());
        timeoutTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // noop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // noop
            }

            @Override
            public void afterTextChanged(Editable s) {
                timeoutValueHolder.setTimeoutString(s.toString());
            }
        });

        return view;
    }

    @Override
    public void onDelete(Contact contact) {
        contactsListViewModel.delete(contact);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    Contact contact = readSelectedContact(data);
                    contactsListViewModel.insert(contact);
                    break;
                default:
                    Log.e("ContactListFragment", "Unexpected value: " + requestCode);
            }
        } else {
            Log.e("ContactListFragment", "Failed to pick contact");
        }
    }

    private void openSystemContactList() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }

    private Contact readSelectedContact(Intent data) {
        Cursor cursor = null;
        try {
            Uri uri = data.getData();
            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String phoneNo = cursor.getString(phoneIndex);
            int nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String name = cursor.getString(nameIndex);
            return new Contact(phoneNo, name);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO error handling
        } finally {
            cursor.close();
        }
        return null;
    }

    private void saveEditedText() {
        String newMessageText = messageTextEdit.getText().toString();
        emergencyTextService.updateMessageText(newMessageText);
        messageText.setText(newMessageText);
        toggleMessageTextView(false);
    }

    private void cancelTextEditing() {
        toggleMessageTextView(false);
    }

    private void setMessageTextEditable() {
        messageTextEdit.setText(emergencyTextService.getMessageText());
        toggleMessageTextView(true);
    }

    private void toggleMessageTextView(boolean editable) {
        int edit = editable ? View.VISIBLE : View.GONE;
        int view = editable ? View.GONE : View.VISIBLE;

        editTextButton.setVisibility(view);
        messageText.setVisibility(view);

        messageTextEdit.setVisibility(edit);
        saveTextButton.setVisibility(edit);
        cancelEditTextButton.setVisibility(edit);
    }
}