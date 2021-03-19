package com.learntodroid.simplealarmclock.contactslist;

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
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.data.contact.Contact;

import java.util.List;

public class ContactsListFragment extends Fragment implements OnManageContactListener {
    private static final int RESULT_PICK_CONTACT = 1;

    private ContactRecyclerViewAdapter contactRecyclerViewAdapter;
    private ContactsListViewModel contactsListViewModel;
    private RecyclerView contactsRecyclerView;
    private Button addContact;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listcontacts, container, false);

        contactsRecyclerView = view.findViewById(R.id.fragment_listcontacts_recylerView);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactsRecyclerView.setAdapter(contactRecyclerViewAdapter);

        addContact = view.findViewById(R.id.fragment_listcontacts_addContact);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSystemContactList();
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
}