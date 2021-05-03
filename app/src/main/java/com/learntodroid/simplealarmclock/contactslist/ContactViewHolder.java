package com.learntodroid.simplealarmclock.contactslist;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.learntodroid.simplealarmclock.data.contact.Contact;
import com.learntodroid.simplealarmclock.databinding.ItemContactBinding;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    private final ItemContactBinding binding;
    private final OnManageContactListener listener;

    public ContactViewHolder(ItemContactBinding binding, OnManageContactListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    public void bind(Contact contact) {
        binding.itemContactName.setText(contact.getContactName());
        binding.itemContactPhoneNumber.setText(contact.getPhoneNumber());

        binding.itemContactDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDelete(contact);
            }
        });
    }
}
