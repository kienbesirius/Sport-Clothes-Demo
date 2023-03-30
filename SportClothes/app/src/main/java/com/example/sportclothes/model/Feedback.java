package com.example.sportclothes.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.databinding.Bindable;

public class Feedback extends BaseObservable {

    private String name;
    private String phone;
    private String email;
    private String comment;

    public Feedback(String name, String phone, String email, String comment) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.comment = comment;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        notifyPropertyChanged(BR.comment);
    }
}
