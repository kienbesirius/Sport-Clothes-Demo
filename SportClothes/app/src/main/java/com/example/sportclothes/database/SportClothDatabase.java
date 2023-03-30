package com.example.sportclothes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.sportclothes.model.SportCloth;

@Database(entities = SportCloth.class, version = 1)
public abstract class SportClothDatabase extends RoomDatabase {

    private static final String DB_NAME = "sport_clothes.db";

    public static SportClothDatabase instance;

    public static synchronized SportClothDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    SportClothDatabase.class,
                    DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract SportClothDAO sportClothDAO();
}
