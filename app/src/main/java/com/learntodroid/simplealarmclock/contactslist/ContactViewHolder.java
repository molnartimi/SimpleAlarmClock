package com.learntodroid.simplealarmclock.contactslist;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.alarmslist.OnManageAlarmListener;
import com.learntodroid.simplealarmclock.data.contact.Contact;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    private TextView contactName;
    private TextView phoneNumber;
    private Button contactDeleteButton;

    private OnManageContactListener listener;

    public ContactViewHolder(@NonNull View itemView, OnManageContactListener listener) {
        super(itemView);

        contactName = itemView.findViewById(R.id.item_contact_name);
        phoneNumber = itemView.findViewById(R.id.item_contact_phoneNumber);
        contactDeleteButton = itemView.findViewById(R.id.item_contact_delete_button);

        this.listener = listener;
    }

    public void bind(Contact contact) {
        contactName.setText(contact.getContactName());
        phoneNumber.setText(contact.getPhoneNumber());

        contactDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDelete(contact);
            }
        });
    }
}
