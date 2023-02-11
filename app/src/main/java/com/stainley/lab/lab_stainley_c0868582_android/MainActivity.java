package com.stainley.lab.lab_stainley_c0868582_android;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.stainley.lab.lab_stainley_c0868582_android.adapter.PlaceRecylerViewAdapter;
import com.stainley.lab.lab_stainley_c0868582_android.databinding.ActivityMainBinding;
import com.stainley.lab.lab_stainley_c0868582_android.model.Place;
import com.stainley.lab.lab_stainley_c0868582_android.view.MapsActivity;
import com.stainley.lab.lab_stainley_c0868582_android.viewmodel.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private PlaceViewModel placeViewModel;
    private PlaceRecylerViewAdapter adapter;
    private List<Place> places = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.addPlaceBtn.setOnClickListener(this::addNewFavoritePlace);

        placeViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(PlaceViewModel.class);
        setTitle("Favorites Place");
        RecyclerView recyclerView = binding.listFavoritePlaces;

        adapter = new PlaceRecylerViewAdapter(places);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        placeViewModel.getAllPlaces().observe(this, places -> {
            this.places.clear();
            this.places.addAll(places);
            adapter.notifyItemChanged(places.size());
        });
    }

    public void addNewFavoritePlace(View view) {
        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyItemChanged(places.size());
    }
}