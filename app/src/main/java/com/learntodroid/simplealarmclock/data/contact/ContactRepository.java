package com.learntodroid.simplealarmclock.data.contact;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactRepository {
    private ContactDao contactDao;
    private LiveData<List<Contact>> contactsLiveData;

    public ContactRepository(Application application) {
        ContactDatabase db = ContactDatabase.getDatabase(application);
        contactDao = db.contactDao();
        contactsLiveData = contactDao.getContactsLiveData();
    }

    public void insert(Contact contact) {
        ContactDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.insert(contact);
        });
    }

    public void update(Contact contact) {
        ContactDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.update(contact);
        });
    }

    public LiveData<List<Contact>> getContactsLiveData() {
        return contactsLiveData;
    }

    public List<Contact> getContacts() {
        return contactDao.getContacts();
    }

    public void delete(Contact contact) {
        ContactDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.delete(contact);
        });
    }
}
