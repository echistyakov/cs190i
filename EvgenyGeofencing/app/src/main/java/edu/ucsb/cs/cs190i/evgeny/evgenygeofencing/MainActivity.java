package edu.ucsb.cs.cs190i.evgeny.evgenygeofencing;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapClickListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {

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
    private LatLng currentLoc = DEFAULT_LOC;
    private int currentRad = DEFAULT_RAD;
    private boolean geofenceEnabled = false;


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

        // Set up button
        Button geofenceButton = (Button) findViewById(R.id.geofenceButton);
        geofenceButton.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker
        marker = map.addMarker(new MarkerOptions().position(currentLoc).title("Marker").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

        // Add a circle
        circle = map.addCircle(new CircleOptions().center(currentLoc).radius(currentRad).strokeColor(Color.BLUE).fillColor(Color.BLUE));

        // Set map click listener
        map.setOnMapClickListener(this);

        // Move camera to marker
        map.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Save new location
        currentLoc = latLng;

        // Reset marker
        marker.setPosition(latLng);

        // Reset circle
        circle.setCenter(latLng);
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
        // Green circle
        if (circle != null) {
            circle.setFillColor(Color.TRANSPARENT);
            circle.setStrokeColor(Color.GREEN);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Blue circle
        if (circle != null) {
            circle.setFillColor(Color.BLUE);
            circle.setStrokeColor(Color.BLUE);
        }
    }

    @Override
    public void onClick(View v) {
        Button geofenceButton = (Button) v;
        if (geofenceEnabled) {
            // Update circle color
            circle.setFillColor(Color.BLUE);
            circle.setStrokeColor(Color.BLUE);
            // Update marker color
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            // Update button text
            geofenceButton.setText(R.string.set_geofence);
            // Enable map clicks
            map.setOnMapClickListener(this);
            // TODO: edit geofence

        } else {
            // Update circle color
            circle.setFillColor(Color.BLUE);
            circle.setStrokeColor(Color.GREEN);
            // Update marker color
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            // Update button text
            geofenceButton.setText(R.string.edit_geofence);
            // Disable map clicks
            map.setOnMapClickListener(null);
            // TODO: set geofence
        }
        // Flip the flag
        geofenceEnabled = !geofenceEnabled;
    }
}
