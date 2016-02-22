package edu.ucsb.cs.cs190i.evgeny.evgenygeofencing;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

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
    private static final String GEO_TEXT = "LAT: %f, LON: %f, RAD: %dm";
    private static final int COLOR_TRANSPARENT_BLUE = Color.argb(96, 0, 0, 255);

    // Layout objects
    private TextView geofenceTextView = null;
    private SeekBar radiusBar = null;
    private Button geofenceButton = null;


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

        // Get views
        geofenceTextView = (TextView) findViewById(R.id.geofenceText);
        radiusBar = (SeekBar) findViewById(R.id.radiusBar);
        geofenceButton = (Button) findViewById(R.id.geofenceButton);

        // Extract saved instance state if exists
        if (savedInstanceState != null) {
            LatLng loc = savedInstanceState.getParcelable("currentLoc");
            // Location
            currentLoc = (loc != null) ? loc : DEFAULT_LOC;
            // Radius
            currentRad = savedInstanceState.getInt("currentRad", currentRad);
            // Geofence flag
            geofenceEnabled = savedInstanceState.getBoolean("geofenceEnabled", geofenceEnabled);
        }

        // Set up radius bar
        radiusBar.setMax(MAX_RAD);

        // Set geo text
        updateGeoText();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save location
        outState.putParcelable("currentLoc", currentLoc);
        // Save radius
        outState.putInt("currentRad", currentRad);
        // Save geofence flag
        outState.putBoolean("geofenceEnabled", geofenceEnabled);
    }

    private void updateGeoText() {
        String geoText = String.format(GEO_TEXT, currentLoc.latitude, currentLoc.longitude, currentRad);
        geofenceTextView.setText(geoText);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker
        marker = map.addMarker(new MarkerOptions().position(currentLoc).title("Marker").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        // Add a circle
        circle = map.addCircle(new CircleOptions().center(currentLoc).radius(currentRad).strokeColor(Color.BLUE).fillColor(COLOR_TRANSPARENT_BLUE));
        // Set map click listener
        map.setOnMapClickListener(this);
        // Move camera to marker
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));
        // Add listeners
        radiusBar.setOnSeekBarChangeListener(this);
        geofenceButton.setOnClickListener(this);
        // Set radius
        radiusBar.setProgress(currentRad);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Save new location
        currentLoc = latLng;
        // Reset marker
        marker.setPosition(latLng);
        // Reset circle
        circle.setCenter(latLng);
        // Update geo text
        updateGeoText();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Save new location
        currentRad = progress;
        // Set radius
        circle.setRadius(progress);
        // Update geo text
        updateGeoText();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Make circle green
        circle.setStrokeColor(Color.GREEN);
        circle.setFillColor(Color.TRANSPARENT);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Make circle blue
        circle.setStrokeColor(Color.BLUE);
        circle.setFillColor(COLOR_TRANSPARENT_BLUE);
    }

    @Override
    public void onClick(View v) {
        if (geofenceEnabled) {
            // Enable map clicks
            map.setOnMapClickListener(this);
            // Enable radius bar
            radiusBar.setEnabled(true);
            // Update button text
            geofenceButton.setText(R.string.set_geofence);
            // Update marker color
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            // Update circle color
            circle.setStrokeColor(Color.BLUE);
            circle.setFillColor(COLOR_TRANSPARENT_BLUE);
            // TODO: edit geofence

        } else {
            // Disable map clicks
            map.setOnMapClickListener(null);
            // Disable radius bar
            radiusBar.setEnabled(false);
            // Update button text
            geofenceButton.setText(R.string.edit_geofence);
            // Update marker color
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            // Update circle color
            circle.setStrokeColor(Color.GREEN);
            circle.setFillColor(COLOR_TRANSPARENT_BLUE);

            // TODO: set geofence
        }
        // Flip the flag
        geofenceEnabled = !geofenceEnabled;
    }
}
