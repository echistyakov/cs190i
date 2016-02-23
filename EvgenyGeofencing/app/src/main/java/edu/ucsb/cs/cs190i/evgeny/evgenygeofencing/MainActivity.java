package edu.ucsb.cs.cs190i.evgeny.evgenygeofencing;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapClickListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Map objects
    private GoogleMap map = null;
    private Marker marker = null;
    private Circle circle = null;
    private Polyline polyline = null;

    // Constants
    private static final double DEFAULT_LAT = 34.412612;
    private static final double DEFAULT_LON = -119.848411;
    private static final LatLng DEFAULT_LOC = new LatLng(DEFAULT_LAT, DEFAULT_LON);  // UCSB Location
    private static final int MAX_RAD = 2000;
    private static final int DEFAULT_RAD = 500;
    private static final String GEO_TEXT = "LAT: %f, LON: %f, RAD: %dm";
    private static final int COLOR_TRANSPARENT_BLUE = Color.argb(96, 0, 0, 255);
    private static final String GEOFENCE_ID = "cs190i geofence";

    // Layout objects
    private TextView geofenceTextView = null;
    private SeekBar radiusBar = null;
    private Button geofenceButton = null;

    private GoogleApiClient googleApiClient = null;
    private PermissionManager permissionManager = null;


    // Variables
    private LatLng currentLoc = DEFAULT_LOC;
    private int currentRad = DEFAULT_RAD;
    private boolean geofenceEnabled = false;
    private ArrayList<LatLng> trajectory = new ArrayList<>();


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

        // Initialize permissions manager
        permissionManager = new PermissionManager(this);

        // Restore state if exists
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        // Set up radius bar
        radiusBar.setMax(MAX_RAD);

        // Set control bar geofence mode
        setControlBarGeofenceMode();

        // Set geo text
        updateGeoText();

        // Build Google API client
        buildGoogleApiClient();

        // Request permissions if missing
        permissionManager.requestPermissionsIfMissing();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Listen for location updates
        addLocationListener();
        // Google API client connect
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Remove location updates listener
        removeLocationListener();
        // Google API client disconnect
        googleApiClient.disconnect();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save location
        outState.putParcelable("currentLoc", currentLoc);
        // Save radius
        outState.putInt("currentRad", currentRad);
        // Save geofence flag
        outState.putBoolean("geofenceEnabled", geofenceEnabled);
        // Save trajectory
        outState.putParcelableArrayList("trajectory", trajectory);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Location
        if (savedInstanceState.containsKey("currentLoc")) {
            currentLoc = savedInstanceState.getParcelable("currentLoc");
        }
        // Radius
        currentRad = savedInstanceState.getInt("currentRad", currentRad);
        // Geofence flag
        geofenceEnabled = savedInstanceState.getBoolean("geofenceEnabled", geofenceEnabled);
        // Trajectory
        if (savedInstanceState.containsKey("trajectory")) {
            trajectory = savedInstanceState.getParcelableArrayList("trajectory");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker
        marker = map.addMarker(
                new MarkerOptions()
                        .position(currentLoc)
                        .title("Marker")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        // Add a circle
        circle = map.addCircle(
                new CircleOptions()
                        .center(currentLoc)
                        .radius(currentRad)
                        .strokeColor(Color.BLUE)
                        .fillColor(COLOR_TRANSPARENT_BLUE));
        // Add a polyline
        polyline = map.addPolyline(
                new PolylineOptions()
                        .color(Color.BLUE)
                        .addAll(trajectory));

        // Move camera to marker
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));
        // Set map geofence mode
        setMapGeofenceMode();
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
        // Flip the flag
        geofenceEnabled = !geofenceEnabled;

        if (geofenceEnabled) {
            // Create geofence
            Geofence geofence = new Geofence.Builder()
                    .setRequestId(GEOFENCE_ID)
                    .setCircularRegion(currentLoc.latitude, currentLoc.longitude, currentRad)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .build();
            // Create geofence request
            GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .addGeofence(geofence)
                    .build();
            // Add geofence
            try {
                LocationServices.GeofencingApi.addGeofences(
                        googleApiClient,
                        geofencingRequest,
                        getGeofencePendingIntent()
                );
            } catch (SecurityException e) {
                geofenceEnabled = !geofenceEnabled;
                return;
            }
        } else {
            // Remove geofence
            LocationServices.GeofencingApi.removeGeofences(
                    googleApiClient,
                    getGeofencePendingIntent()
            );
        }

        // Set geofence mode on Control Bar and Map
        setControlBarGeofenceMode();
        setMapGeofenceMode();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PermissionManager.PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addLocationListener();
            } else {
                Toast.makeText(this, "GPS permission denied - cannot track location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng newPoint = new LatLng(location.getLatitude(), location.getLongitude());
        trajectory.add(newPoint);
        if (polyline != null) {
            polyline.setPoints(trajectory);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onConnected(Bundle bundle) {}
    @Override
    public void onConnectionSuspended(int i) {}
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    private void updateGeoText() {
        String geoText = String.format(GEO_TEXT, currentLoc.latitude, currentLoc.longitude, currentRad);
        geofenceTextView.setText(geoText);
    }

    private void setControlBarGeofenceMode() {
        /* Control bar related changes */
        if (geofenceEnabled) {
            // Disable radius bar
            radiusBar.setEnabled(false);
            // Update button text
            geofenceButton.setText(R.string.edit_geofence);
        } else {
            // Enable radius bar
            radiusBar.setEnabled(true);
            // Update button text
            geofenceButton.setText(R.string.set_geofence);
        }
    }

    private void setMapGeofenceMode() {
        /* Map related changes */
        if (geofenceEnabled) {
            // Disable map clicks
            map.setOnMapClickListener(null);
            // Update marker color
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            // Update circle color
            circle.setStrokeColor(Color.GREEN);
            circle.setFillColor(COLOR_TRANSPARENT_BLUE);
        } else {
            // Enable map clicks
            map.setOnMapClickListener(this);
            // Update marker color
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            // Update circle color
            circle.setStrokeColor(Color.BLUE);
            circle.setFillColor(COLOR_TRANSPARENT_BLUE);
        }
    }

    private void addLocationListener() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            // Do nothing
        }
    }

    private void removeLocationListener() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            // Do nothing
        }
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
