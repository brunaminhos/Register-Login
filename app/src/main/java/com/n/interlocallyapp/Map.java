package com.n.interlocallyapp;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.n.interlocallyapp.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        // Add markers for stores and move the camera
        LatLng linkBrazil = new LatLng(52.660846, -8.631604);
        LatLng yongXing = new LatLng(52.661634, -8.626570);
        LatLng asianArabic = new LatLng(52.660534, -8.624203);
        mMap.addMarker(new MarkerOptions().position(linkBrazil).title("Marker in Link Brazil"));
        mMap.addMarker(new MarkerOptions().position(yongXing).title("Marker in Yong Xing Supermarket"));
        mMap.addMarker(new MarkerOptions().position(asianArabic).title("Marker in Asian Arabic Foods"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(yongXing));
    }
}