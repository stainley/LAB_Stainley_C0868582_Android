package com.stainley.lab.lab_stainley_c0868582_android.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.stainley.lab.lab_stainley_c0868582_android.db.AppDatabase;
import com.stainley.lab.lab_stainley_c0868582_android.model.Place;
import com.stainley.lab.lab_stainley_c0868582_android.model.PlaceDao;

import java.util.List;

public class PlaceRepository {
    private PlaceDao placeDao;

    public PlaceRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        placeDao = db.placeDao();
    }

    public void savePlace(Place place) {
        AppDatabase.databaseWriterExecutor.execute(() -> placeDao.savePlace(place));
    }

    public void deletePlace(Place place) {
        AppDatabase.databaseWriterExecutor.execute(() -> placeDao.deletePlace(place));
    }

    public void updatePlace(Place place) {
        AppDatabase.databaseWriterExecutor.execute(() -> placeDao.updatePlace(place));
    }

    public LiveData<List<Place>> getAllPlaces() {
        return placeDao.getAllPlaces();
    }
}
