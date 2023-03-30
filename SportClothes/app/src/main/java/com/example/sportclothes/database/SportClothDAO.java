package com.example.sportclothes.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sportclothes.model.SportCloth;

import java.util.List;

@Dao
public interface SportClothDAO {

    @Insert
    void insert(SportCloth sportCloth);

    @Update
    void update(SportCloth sportCloth);

    @Delete
    void delete(SportCloth sportCloth);

    @Query("DELETE FROM sport_cloth")
    void deleteAll();

    @Query("SELECT * FROM sport_cloth")
    List<SportCloth> getListSportClothesCart();

    @Query("SELECT * FROM sport_cloth WHERE id = :sportCloth_ID")
    List<SportCloth> checkSportClothInCart(int sportCloth_ID);
}
