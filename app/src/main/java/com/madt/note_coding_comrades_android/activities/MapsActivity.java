package com.madt.note_coding_comrades_android.activities;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.madt.note_coding_comrades_android.R;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng latLangNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        String address = "Could not find the address"; // default address message
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault()); // sets the geocoder object
        latLangNote = new LatLng(getIntent().getDoubleExtra("note_latitude",0) ,  getIntent().getDoubleExtra("note_longitude",0));

        try {
            //gets the address list
            List<Address> addressList = geocoder.getFromLocation(latLangNote.latitude, latLangNote.longitude, 1);
            if (addressList != null && addressList.size() > 0) { // if the addressList gets a result
                address = ""; // empty the address message
                // street name
                if (addressList.get(0).getThoroughfare() != null) // if there is a street name
                    address += addressList.get(0).getThoroughfare() + ", "; // add the street name
                if (addressList.get(0).getPostalCode() != null)  // if there is a postal code name
                    address += addressList.get(0).getPostalCode() + ", "; // add the postal code name
                if (addressList.get(0).getLocality() != null)  // if there is a city name
                    address += addressList.get(0).getLocality() + ", "; // add the city name
                if (addressList.get(0).getAdminArea() != null)  // if there is a province name
                    address += addressList.get(0).getAdminArea(); // add the province name

                mMap.addMarker(new MarkerOptions()
                        .position(latLangNote)
                        .title("Your note Location")
                        .snippet(address)
                );
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLangNote, 15));
            }
        } catch (Exception e) {
            mMap.addMarker(new MarkerOptions()
                    .position(latLangNote)
                    .title("Your note Location")
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLangNote, 15));
            e.printStackTrace(); // catch the error
        }

    }
}