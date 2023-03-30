package com.example.sportclothes;

import android.app.Application;
import android.content.Context;

import com.example.sportclothes.constant.Constant;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SCApplication extends Application {

    private FirebaseDatabase mFirebaseDatabase;

    public static SCApplication get(Context context) {
        return (SCApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance(Constant.FIREBASE_URL);
    }

    public DatabaseReference getSportClothesDatabaseReference() {
        return mFirebaseDatabase.getReference("/sport_clothes");
    }

    public DatabaseReference getFeedbackDatabaseReference() {
        return mFirebaseDatabase.getReference("/feedback");
    }

    public DatabaseReference getBookingDatabaseReference() {
        return mFirebaseDatabase.getReference("/booking");
    }
}
