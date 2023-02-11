package com.stainley.lab.lab_stainley_c0868582_android.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stainley.lab.lab_stainley_c0868582_android.R;
import com.stainley.lab.lab_stainley_c0868582_android.databinding.ActivityMapsBinding;
import com.stainley.lab.lab_stainley_c0868582_android.model.Place;
import com.stainley.lab.lab_stainley_c0868582_android.viewmodel.PlaceViewModel;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = MapsActivity.class.getName();
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final int REQUEST_CODE = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private Place place;
    private List<Marker> markers = new ArrayList<>();
    private Place myFavoritePlace;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        Toolbar toolbar = binding.toolbar;
        toolbar.setBackgroundColor(getResources().getColor(R.color.purple_200));
        setActionBar(toolbar);
        if (getActionBar() != null) getActionBar().setDisplayHomeAsUpEnabled(true);

        binding.savePlace.setOnClickListener(this::savePlace);

        searchView = binding.searchOnMap;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();

                List<Address> addressList = new ArrayList<>();
                place = new Place();
                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    // on below line we are creating and initializing a geo coder.

                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {

                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    Address address = addressList.get(0);

                    place.setLatitude(address.getLatitude());
                    place.setLongitude(address.getLongitude());

                    if (address.getLocality() != null) {
                        place.setLocality(address.getLocality());
                    }

                    if (address.getAdminArea() != null) {
                        place.setAdminArea(address.getAdminArea());
                    }

                    if (address.getThoroughfare() != null) {
                        place.setThoroughfare(address.getThoroughfare());
                    }

                    if (address.getPostalCode() != null) {
                        place.setPostalCode(address.getPostalCode());
                    }

                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Intent myPlaceIntent = getIntent();
        myFavoritePlace = (Place) myPlaceIntent.getSerializableExtra("my_place");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = location -> {

        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_satellite:

                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;

            case R.id.menu_terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                return true;
            case R.id.menu_hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.menu_normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            default:
                break;
        }
        return false;
    }

    public void savePlace(View view) {

        PlaceViewModel placeViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PlaceViewModel.class);
        placeViewModel.insertPlace(place);
        Toast.makeText(this, "Places has been saved", Toast.LENGTH_SHORT).show();

        finish();
    }

    public void updatePlace(Place newFavoritePlace) {
        PlaceViewModel placeViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PlaceViewModel.class);
        placeViewModel.updatePlace(newFavoritePlace);
        Toast.makeText(this, "Updated place", Toast.LENGTH_SHORT).show();
    }

    private void clearMarker() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMinZoomPreference(13);
        LatLng placeOnMap;
        if (myFavoritePlace != null) {
            placeOnMap = new LatLng(myFavoritePlace.getLatitude(), myFavoritePlace.getLongitude());
            StringBuilder address = new StringBuilder();
            address.append(myFavoritePlace.getAdminArea()).append(", ").append(myFavoritePlace.getLocality()).append(", ").append(myFavoritePlace.getPostalCode()).append(", ").append(myFavoritePlace.getThoroughfare());

            Marker marker = mMap.addMarker(new MarkerOptions().position(placeOnMap).title(address.toString()).draggable(true));
            markers.add(marker);
            assert marker != null;
            marker.showInfoWindow();
        } else {
            placeOnMap = new LatLng(43.6532, -79.3832);
            Marker marker = mMap.addMarker(new MarkerOptions().position(placeOnMap).title("My Current Place"));
            markers.add(marker);
            assert marker != null;
            marker.showInfoWindow();

        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(placeOnMap));


        mMap.setOnMapLongClickListener(latLng -> {
            clearMarker();
            MarkerOptions favoriteMarkerOptions = new MarkerOptions();
            favoriteMarkerOptions.draggable(true);
            favoriteMarkerOptions.position(latLng);
            favoriteMarkerOptions.infoWindowAnchor(52, 52);
            favoriteMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            Log.i(TAG, "onLongClickListener: " + latLng);
            placeDetailMarker(latLng, favoriteMarkerOptions);
        });

        // Drag and Update marker
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                //myFavoritePlace


                MarkerOptions favoriteMarkerOptions = new MarkerOptions();
                favoriteMarkerOptions.draggable(true);
                favoriteMarkerOptions.position(marker.getPosition());
                favoriteMarkerOptions.infoWindowAnchor(52, 52);
                favoriteMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                placeDetailMarker(marker.getPosition(), favoriteMarkerOptions);

                myFavoritePlace = place;
                // TODO: update in the DATABASE
                updatePlace(myFavoritePlace);

            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {
            }
        });

    }

    private void placeDetailMarker(LatLng latLng, MarkerOptions favoriteMarkerOptions) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        StringBuilder address = new StringBuilder();
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                address.append("\n");
                place = new Place();
                place.setLatitude(latLng.latitude);
                place.setLongitude(latLng.longitude);
                // street name
                if (addressList.get(0).getThoroughfare() != null) {
                    place.setThoroughfare(addressList.get(0).getThoroughfare());
                    address.append(addressList.get(0).getThoroughfare()).append("\n");
                }
                if (addressList.get(0).getLocality() != null) {
                    place.setLocality(addressList.get(0).getLocality());
                    address.append(addressList.get(0).getLocality()).append(" ");
                }

                if (addressList.get(0).getPostalCode() != null) {
                    place.setPostalCode(addressList.get(0).getPostalCode());
                    address.append(addressList.get(0).getPostalCode()).append(" ");
                }
                if (addressList.get(0).getAdminArea() != null) {
                    place.setAdminArea(addressList.get(0).getAdminArea());
                    address.append(addressList.get(0).getAdminArea());
                }
            }
        } catch (Exception e) {
            address.append("Could not find the address");
            e.printStackTrace();
            place = new Place();
            place.setLatitude(latLng.latitude);
            place.setLongitude(latLng.longitude);
            place.setLocality(new Date().toString());
            favoriteMarkerOptions.title(place.getLocality());
            Marker marker = mMap.addMarker(favoriteMarkerOptions);
            markers.clear();

            markers.add(marker);
            assert marker != null;
            marker.showInfoWindow();
            return;
        }

        favoriteMarkerOptions.title(address.toString());
        Marker marker = mMap.addMarker(favoriteMarkerOptions);
        markers.clear();
        markers.add(marker);
        assert marker != null;
        marker.showInfoWindow();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
}