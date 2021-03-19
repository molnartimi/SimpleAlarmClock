package com.learntodroid.simplealarmclock.contactslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.learntodroid.simplealarmclock.data.contact.Contact;
import com.learntodroid.simplealarmclock.data.contact.ContactRepository;

import java.util.List;

public class ContactsListViewModel extends AndroidViewModel {
    private ContactRepository contactRepository;
    private LiveData<List<Contact>> contactsLiveData;

    public ContactsListViewModel(@NonNull Application application) {
        super(application);

        contactRepository = new ContactRepository(application);
        contactsLiveData = contactRepository.getContactsLiveData();
    }

    public void update(Contact contact) {
        contactRepository.update(contact);
    }

    public LiveData<List<Contact>> getContactsLiveData() {
        return contactsLiveData;
    }

    public void delete(Contact contact) {
        contactRepository.delete(contact);
    }

    public void insert(Contact contact) {
        contactRepository.insert(contact);
    }
}
