package com.stainley.lab.lab_stainley_c0868582_android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.stainley.lab.lab_stainley_c0868582_android.model.Place;
import com.stainley.lab.lab_stainley_c0868582_android.repository.PlaceRepository;

import java.util.List;

public class PlaceViewModel extends AndroidViewModel {

    private final PlaceRepository repository;

    public PlaceViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaceRepository(application);
    }

    public void insertPlace(Place place) {
        repository.savePlace(place);
    }

    public void updatePlace(Place place) {
        repository.updatePlace(place);
    }

    public void deletePlace(Place place) {
        repository.deletePlace(place);
    }

    public LiveData<List<Place>> getAllPlaces() {
        return repository.getAllPlaces();
    }
}
