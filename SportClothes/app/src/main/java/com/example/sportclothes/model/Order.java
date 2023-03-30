package com.example.sportclothes.model;


import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.databinding.Bindable;

import java.io.Serializable;

public class Order extends BaseObservable implements Serializable {

    private long id;
    private String name;
    private String phone;
    private String address;
    private int amount;
    private String sportCloth;
    private int payment;

    public Order() {
    }

    public Order(long id, String name, String phone, String address, int amount, String sportCloth, int payment) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.amount = amount;
        this.sportCloth = sportCloth;
        this.payment = payment;
    }

    @Bindable
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
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
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        notifyPropertyChanged(BR.amount);
    }

    @Bindable
    public String getSportCloth() {
        return sportCloth;
    }

    public void setSportClothes(String sportCloth) {
        this.sportCloth = sportCloth;
        notifyPropertyChanged(BR.sportCloth);
    }

    @Bindable
    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
        notifyPropertyChanged(BR.payment);
    }
}
