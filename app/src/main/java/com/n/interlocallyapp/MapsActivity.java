package com.n.interlocallyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.n.interlocallyapp.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private TextView textViewData;
    private Intent productInfoIntent;
    private Bundle args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productInfoIntent = getIntent();
        args = productInfoIntent.getExtras();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng ll = productInfoIntent.getParcelableExtra("longLat_dataProvider");
        Log.d("Location", "location: " + ll.latitude + " " + ll.longitude);

        LatLng location = new LatLng(ll.latitude, ll.longitude);
        mMap.addMarker(new MarkerOptions().position(location).title("Map Marker"));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        String shopName = productInfoIntent.getExtras().getString("name_dataProvider");
        textViewData = findViewById(R.id.textViewData);
        textViewData.setText(shopName);

        Log.d("TAG_MAP", "OUTSIDE MARKER");

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker arg0) {

                args.putParcelable("longLat_dataProvider", location);
                args.putString("name_dataProvider", shopName);

                productInfoIntent = new Intent(MapsActivity.this, ProductInfo.class);
                productInfoIntent.putExtras(args);
                startActivity(productInfoIntent);
                Log.d("TAG_MAP", "INSIDE MARKER");
                return true;
            }

        });
    }
}