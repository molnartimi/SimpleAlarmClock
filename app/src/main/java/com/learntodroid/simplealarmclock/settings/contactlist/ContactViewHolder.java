package com.learntodroid.simplealarmclock.settings.contactlist;

import android.app.AlertDialog;

import androidx.recyclerview.widget.RecyclerView;

import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.data.contact.Contact;
import com.learntodroid.simplealarmclock.databinding.ItemContactBinding;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    private final ItemContactBinding binding;
    private final OnManageContactListener listener;
    private Contact contact;

    public ContactViewHolder(ItemContactBinding binding, OnManageContactListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    public void bind(Contact contact) {
        this.contact = contact;
        binding.contactName.setText(contact.getContactName());
        binding.contactPhoneNumber.setText(contact.getPhoneNumber());
        binding.getRoot().setOnClickListener(v -> deleteConfirmPopup());
    }

    private void deleteConfirmPopup() {

        new AlertDialog.Builder(itemView.getContext())
                .setTitle(binding.contactName.getText() + " "
                        + itemView.getContext().getResources().getString(R.string.delete_contact_confirmation_title_part))
                .setMessage(R.string.delete_contact_confirmation_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.delete, (d, b) -> listener.onDelete(contact))
                .setNegativeButton(R.string.cancel, null).show();
    }
}
