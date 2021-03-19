package com.learntodroid.simplealarmclock.data.contact;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert
    void insert(Contact contact);

    @Query("DELETE FROM contact_table")
    void deleteAll();

    @Query("SELECT * FROM contact_table ORDER BY contactName ASC")
    LiveData<List<Contact>> getContactsLiveData();

    @Query("SELECT * FROM contact_table ORDER BY contactName ASC")
    List<Contact> getContacts();

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);
}
