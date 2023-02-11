package com.stainley.lab.lab_stainley_c0868582_android.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaceDao {

    @Insert
    void savePlace(Place place);

    @Delete
    void deletePlace(Place place);

    @Update
    void updatePlace(Place place);

    @Query("SELECT * FROM FAVORITE_PLACE")
    LiveData<List<Place>> getAllPlaces();

    @Query("SELECT * FROM FAVORITE_PLACE WHERE id = :id")
    LiveData<Place> getPlaceById(Long id);
}
