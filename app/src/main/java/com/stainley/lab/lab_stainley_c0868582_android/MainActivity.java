package com.stainley.lab.lab_stainley_c0868582_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.stainley.lab.lab_stainley_c0868582_android.databinding.ActivityMainBinding;
import com.stainley.lab.lab_stainley_c0868582_android.view.MapsActivity;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.addPlaceBtn.setOnClickListener(this::addNewFavoritePlace);
    }

    public void addNewFavoritePlace(View view) {
        Intent mapIntent = new Intent(this, MapsActivity.class);

        startActivity(mapIntent);
    }


}