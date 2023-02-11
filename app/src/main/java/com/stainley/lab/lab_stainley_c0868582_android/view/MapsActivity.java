package com.stainley.lab.lab_stainley_c0868582_android.view;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = MapsActivity.class.getName();
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final int REQUEST_CODE = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private double latitude;
    private double longitude;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Intent myPlaceIntent = getIntent();
        Place myFavoritePlace  = (Place) myPlaceIntent.getSerializableExtra("my_place");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = location -> {
            Log.i(TAG, "onLocationChanged: " + location);
            //updateLocationInfo(location);
        };

        // if the permission is granted, we request the update.
        // if the permission is not granted, we request for the access.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lasKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //updateLocationInfo(lasKnownLocation);
        }
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
        mMap.setMinZoomPreference(15);

        LatLng toronto = new LatLng(43.6532, -79.3832);
        //mMap.addMarker(new MarkerOptions().position(toronto).title("Marker in Toronto"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(toronto));


        mMap.setOnMapLongClickListener(latLng -> {
            Log.i(TAG, "onLongClickListener: " + latLng);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            StringBuilder address = new StringBuilder();
            try {
                List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addressList != null && addressList.size() > 0) {
                    address.append("\n");
                    place = new Place();
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
            }


            MarkerOptions favoriteMarkerOptions = new MarkerOptions();
            favoriteMarkerOptions.draggable(true);
            favoriteMarkerOptions.title(address.toString());
            favoriteMarkerOptions.position(latLng);
            favoriteMarkerOptions.snippet("Save favorite place");
            favoriteMarkerOptions.infoWindowAnchor(52, 600);
            favoriteMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mMap.addMarker(favoriteMarkerOptions);


        });

        mMap.setOnMarkerClickListener(marker -> {
            System.out.println(marker.getTitle());
            // TODO: save into the DATABASE
            Log.i(TAG, "onMarkerClicked");


            Toast.makeText(this, "Places has been saved", Toast.LENGTH_SHORT).show();
            Intent intentPlace = new Intent();
            intentPlace.putExtra("favoritePlace", place);

            setResult(Activity.RESULT_OK, intentPlace);
            finish();
            return false;
        });

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