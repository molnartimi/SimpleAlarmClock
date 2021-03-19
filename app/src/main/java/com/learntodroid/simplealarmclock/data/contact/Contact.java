package com.learntodroid.simplealarmclock.data.contact;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_table")
public class Contact {
    @PrimaryKey
    @NonNull
    private String phoneNumber;

    private String contactName;

    public Contact(String phoneNumber, String contactName) {
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getContactName() {
        return contactName;
    }
}
