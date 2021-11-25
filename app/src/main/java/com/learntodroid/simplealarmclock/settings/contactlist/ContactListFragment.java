package com.learntodroid.simplealarmclock.settings.contactlist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.learntodroid.simplealarmclock.data.contact.Contact;
import com.learntodroid.simplealarmclock.databinding.FragmentContactlistBinding;

public class ContactListFragment extends Fragment implements OnManageContactListener {
    private static final int RESULT_PICK_CONTACT = 1;
    private static final String TAG = "ContactListFragment";

    private FragmentContactlistBinding binding;
    private ContactRecyclerViewAdapter contactRecyclerViewAdapter;
    private ContactListViewModel contactListViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactRecyclerViewAdapter = new ContactRecyclerViewAdapter(this);
        contactListViewModel = ViewModelProviders.of(this).get(ContactListViewModel.class);
        contactListViewModel.getContactsLiveData().observe(this, contacts -> {
            if (contacts != null) {
                contactRecyclerViewAdapter.setContacts(contacts);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentContactlistBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.contactListRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.contactListRecycleView.setAdapter(contactRecyclerViewAdapter);

        binding.addContactBtn.setOnClickListener(v -> openSystemContactList());

        return view;
    }

    @Override
    public void onDelete(Contact contact) {
        contactListViewModel.delete(contact);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    Contact contact = readSelectedContact(data);
                    contactListViewModel.insert(contact);
                    break;
                default:
                    Log.e(TAG, "Unexpected value: " + requestCode);
            }
        } else {
            Log.e(TAG, "Failed to pick contact");
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
            assert uri != null;
            cursor = requireActivity().getContentResolver().query(uri, null, null, null, null);
            assert cursor != null;
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

}
