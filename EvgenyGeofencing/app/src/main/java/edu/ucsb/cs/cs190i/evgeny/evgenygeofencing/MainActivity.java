package edu.ucsb.cs.cs190i.evgeny.evgenygeofencing;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapClickListener {

    private GoogleMap map = null;
    private Marker marker = null;
    private Circle circle = null;
    private static final double DEFAULT_LAT = 34.412612;
    private static final double DEFAULT_LON = -119.848411;
    private static final LatLng UCSB_LOC = new LatLng(DEFAULT_LAT, DEFAULT_LON);
    private static final double DEFAULT_RAD = 500;
    private static final String GEO_TEXT = "LAT: {}, LON: {}, RAD: {}m";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker (UCSB)
        marker = map.addMarker(new MarkerOptions().position(UCSB_LOC).title("UCSB"));

        // Add a circle
        circle = map.addCircle(new CircleOptions().center(UCSB_LOC).radius(DEFAULT_RAD).strokeColor(Color.GREEN));

        // Set map click listener
        map.setOnMapClickListener(this);

        // Move camera to marker
        map.moveCamera(CameraUpdateFactory.newLatLng(UCSB_LOC));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Reset marker
        marker.setPosition(latLng);
        marker.setTitle("Marker");

        // Reset circle
        circle.setCenter(latLng);
        // TODO: slider on click update circle radius

        // TODO: do we need this?
        // map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
