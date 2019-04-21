package com.example.b00086142.finalproject.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00086142.finalproject.R;
import com.example.b00086142.finalproject.bean.Walk;
import com.example.b00086142.recorder.persistance.DbTool;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");

    private String TAG = "walking";
    private String INTENT_DISTANCEKEY ="distance";
    private  String INTENT_TIMEKEY = "time";
    private  String INTENT_SPEED = "speed";

    // variable for Google Map API
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;
    private Location mLastKnownLocation;
    private final LatLng mDefaultLocation = new LatLng(1.283333, 103.833333);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    ArrayList<LatLng> list = new ArrayList<LatLng>();


    TextView textView;
    TextView heart_rate;
    TextView speed;
    Button button;
    Button startButton;
    Button endButton;
    StopWatchService stopWatchService;
    boolean mBound = false;
    Intent startWatchIntent;
    Intent stopWatchIntent;

    DatabaseReference exampleRun;
    Calendar c;
    String formattedDate = "";


    int listSize=-1;
    DbTool dbTool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle(getString(R.string.currentWalk));

        //create the view elements
        textView = (TextView) findViewById(R.id.textView);
        heart_rate = (TextView) findViewById(R.id.heart_rate);
        speed = (TextView) findViewById(R.id.speed);

        button = (Button) findViewById(R.id.button2);
        startButton = (Button) findViewById(R.id.buttonStart);
        endButton = (Button) findViewById(R.id.buttonEnd);

        startWatchIntent = new Intent(this, StopWatchService.class);
        stopWatchIntent = new Intent(this, StopWatchService.class);
        c = Calendar.getInstance();
        dbTool = new DbTool(this);

        //add a child node to the db reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rt = database.getReference("user1/walk");
        exampleRun = rt.push();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        // creates the map leading to the onMapReady function being called
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /* when the start button is pressed, start the stopwatch service
         * and bind to that service.
         * */
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startService(startWatchIntent);
                bindService(startWatchIntent, mConnection, Context.BIND_AUTO_CREATE);

                // when the walk has started, take note of the current time.
                formattedDate = df.format(c.getTime());

                getPermissionAndLocationChange();

            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (list.size() == 0) {
                    Toast.makeText(MapsActivity.this, "Please let yourself move", Toast.LENGTH_SHORT).show();
                    return;
                }
                double computedDistance = getDistance();
                long elapsedTime = stopWatchService.getElapsedTime();

                //on the new child node, create these 4 'fields' and insert into the database
//                exampleRun.child("time").setValue(formattedDate);
//                exampleRun.child("distance").setValue(computedDistance);
//                exampleRun.child("arrOfLatLng").setValue(list);
//                exampleRun.child("duration").setValue(elapsedTime);
                Walk walk = new Walk(formattedDate,computedDistance,list,elapsedTime);
                try {
                    double distance = getDistance();
                    walk.setSpeed(3.6 * distance / elapsedTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                exampleRun.setValue(walk);
                stopService(stopWatchIntent);
                unbindService(mConnection);
                mBound = false;

                Intent intentToFinish = new Intent(getApplicationContext(),FinishActivity.class);
                intentToFinish.putExtra(INTENT_DISTANCEKEY,computedDistance);
                intentToFinish.putExtra(INTENT_TIMEKEY,elapsedTime);
                intentToFinish.putExtra(INTENT_SPEED,walk.getSpeed());
                startActivity(intentToFinish);
                finish();

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentToList = new Intent(getApplicationContext(),ListOfWalks.class);
                startActivity(intentToList);

            }

        });

        /**
         * Every one second: display the time that has passed since the walk has started.
         */
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mBound) {
                                    long elapsedTime = stopWatchService.getElapsedTime();
                                    String formattedTime = DateUtils.formatElapsedTime(elapsedTime);
                                    textView.setText(formattedTime);
                                    int i = dbTool.queryLastHeartRate();
                                    heart_rate.setText(""+i+" ");
                                    try {
                                        if (listSize < list.size()) {
                                            double distance = getDistance();
                                            double v = 3.6 * distance / elapsedTime;
                                            speed.setText(String.format("%.2f",v));
                                            listSize = list.size();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();


    }


    protected void onDestroy() {
        super.onDestroy();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            StopWatchService.LocalBinder binder = (StopWatchService.LocalBinder) service;
            stopWatchService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateLocationUI();

        getDeviceLocation();



    }


    @Override
    public void onConnected(Bundle connectionHint) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */


        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }

    }

    /**
     *Loop through the arrayList of latlng
     * and compute the distance between each latlng
     *
     * @return  total distance covered in meters
     */
    private double getDistance() {

        double totalDistance = 0;

        for (int i = 0; i < list.size() - 1; i++) {
            totalDistance = totalDistance + SphericalUtil.computeDistanceBetween(list.get(i), list.get(i + 1));
        }

        return totalDistance;

    }


    /**
     * Check permission then start listening for location changes.
     * When location change, add the latlng in an arraylist.
     */
    public void getPermissionAndLocationChange(){
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //minimum time interval between location updates, in milliseconds. in here, is every 10 seconds.
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0,locationListener );
        }

    }
    LocationListener locationListener = new LocationListener() {
        @Override
        /**
         * Every time the current location of the device change,
         * add the latlng of the current location into an arrayList
         *
         * Display the 'locations' /coordinate the user has walked.
         */
        public void onLocationChanged(Location location) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Toast.makeText(MapsActivity.this, latLng.toString(), Toast.LENGTH_SHORT).show();
            if (list.size() > 0 && !list.get(list.size() - 1).toString().equals(latLng.toString())) {
                list.add(latLng);
            } else {
                list.add(latLng);
            }

            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(5)
                    .color(Color.RED));

            //mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status,
        Bundle extras) {

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
