package edu.ucsb.cs.cs190i.evgeny.evgenygeofencing;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapClickListener, SeekBar.OnSeekBarChangeListener {

    // Map objects
    private GoogleMap map = null;
    private Marker marker = null;
    private Circle circle = null;

    // Constants
    private static final double DEFAULT_LAT = 34.412612;
    private static final double DEFAULT_LON = -119.848411;
    private static final LatLng DEFAULT_LOC = new LatLng(DEFAULT_LAT, DEFAULT_LON);  // UCSB Location
    private static final int MAX_RAD = 2000;
    private static final int DEFAULT_RAD = 500;
    private static final String GEO_TEXT = "LAT: {}, LON: {}, RAD: {}m";

    // Variables
    private static LatLng curretnLoc = DEFAULT_LOC;
    private static int currentRad = DEFAULT_RAD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load maps
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        // Set up slider
        SeekBar radiusBar = (SeekBar) findViewById(R.id.radiusBar);
        radiusBar.setMax(MAX_RAD);
        radiusBar.setProgress(DEFAULT_RAD);
        radiusBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker (UCSB)
        marker = map.addMarker(new MarkerOptions().position(DEFAULT_LOC).title("UCSB"));

        // Add a circle
        circle = map.addCircle(new CircleOptions().center(DEFAULT_LOC).radius(DEFAULT_RAD).strokeColor(Color.GREEN));

        // Set map click listener
        map.setOnMapClickListener(this);

        // Move camera to marker
        map.moveCamera(CameraUpdateFactory.newLatLng(DEFAULT_LOC));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Save new location
        curretnLoc = latLng;

        // Reset marker
        marker.setPosition(latLng);
        marker.setTitle("Marker");

        // Reset circle
        circle.setCenter(latLng);
        // TODO: do we need this? map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Save new location
        currentRad = progress;
        if (circle != null) {
            // Set radius
            circle.setRadius(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO: circle should be green
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO: circle should be blue
    }
}
