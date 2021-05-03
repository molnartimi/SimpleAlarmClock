package com.learntodroid.simplealarmclock.contactslist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learntodroid.simplealarmclock.data.contact.Contact;
import com.learntodroid.simplealarmclock.databinding.ItemContactBinding;

import java.util.ArrayList;
import java.util.List;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    private List<Contact> contacts;
    private OnManageContactListener listener;

    public ContactRecyclerViewAdapter(OnManageContactListener listener) {
        this.contacts = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContactBinding binding =  ItemContactBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ContactViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }
}

