package com.example.sportclothes.model;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Contact extends BaseObservable {

    public static final int FACEBOOK = 0;
    public static final int HOTLINE = 1;
    public static final int GMAIL = 2;
    public static final int SKYPE = 3;
    public static final int YOUTUBE = 4;
    public static final int ZALO = 5;

    private int id;
    private int image;

    public Contact(int id, int image) {
        this.id = id;
        this.image = image;
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }
}
