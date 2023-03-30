package com.example.sportclothes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportclothes.R;
import com.example.sportclothes.constant.GlobalFunction;
import com.example.sportclothes.databinding.ItemContactBinding;
import com.example.sportclothes.model.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private final List<Contact> contactList;
    private final ICallPhone iCallPhone;

    public interface ICallPhone{
        void onClickCallPhone();
    }

    public ContactAdapter(Context context, List<Contact> contactList, ICallPhone iCallPhone) {
        this.context = context;
        this.contactList = contactList;
        this.iCallPhone = iCallPhone;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContactBinding itemContactBinding = ItemContactBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ContactViewHolder(itemContactBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        if (contact == null) {
            return;
        }
        holder.itemContactBinding.imgContact.setImageResource(contact.getImage());
        switch (contact.getId()) {
            case Contact.FACEBOOK:
                holder.itemContactBinding.tvContact.setText(context.getString(R.string.label_facebook));
                break;

            case Contact.HOTLINE:
                holder.itemContactBinding.tvContact.setText(context.getString(R.string.label_call));
                break;

            case Contact.GMAIL:
                holder.itemContactBinding.tvContact.setText(context.getString(R.string.label_gmail));
                break;

            case Contact.SKYPE:
                holder.itemContactBinding.tvContact.setText(context.getString(R.string.label_skype));
                break;

            case Contact.YOUTUBE:
                holder.itemContactBinding.tvContact.setText(context.getString(R.string.label_youtube));
                break;

            case Contact.ZALO:
                holder.itemContactBinding.tvContact.setText(context.getString(R.string.label_zalo));
                break;
        }

        holder.itemContactBinding.layoutItem.setOnClickListener(v -> {
            switch (contact.getId()) {
                case Contact.FACEBOOK:
                    GlobalFunction.onClickOpenFacebook(context);
                    break;

                case Contact.HOTLINE:
                    iCallPhone.onClickCallPhone();
                    break;

                case Contact.GMAIL:
                    GlobalFunction.onClickOpenGmail(context);
                    break;

                case Contact.SKYPE:
                    GlobalFunction.onClickOpenSkype(context);
                    break;

                case Contact.YOUTUBE:
                    GlobalFunction.onClickOpenYoutubeChannel(context);
                    break;

                case Contact.ZALO:
                    GlobalFunction.onClickOpenZalo(context);
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList == null ? 0 : contactList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{
        private final ItemContactBinding itemContactBinding;
        public ContactViewHolder(ItemContactBinding itemContactBinding) {
            super(itemContactBinding.getRoot());
            this.itemContactBinding = itemContactBinding;
        }
    }

    public void release() {
        context = null;
    }
}
