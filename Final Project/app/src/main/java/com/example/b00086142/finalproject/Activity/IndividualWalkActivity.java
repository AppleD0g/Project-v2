package com.example.b00086142.finalproject.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.b00086142.finalproject.R;
import com.example.b00086142.finalproject.bean.Walk;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IndividualWalkActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String TAG = "walkingApp";
    private String INTENT_DATETIMEKEY = "dateTime";
    private static final int DEFAULT_ZOOM = 17;

    GoogleMap mMap;
    Walk walk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_walk);
        setTitle("Details of walk");

        walk = (Walk) getIntent().getSerializableExtra(INTENT_DATETIMEKEY);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ((TextView)findViewById(R.id.textViewDuration)).setText(walk.getDuration()+"");
        ((TextView)findViewById(R.id.textViewDistance)).setText(walk.getDistance()+"");
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//
//        Query query = reference.child("user1").orderByChild("time").equalTo(dateTimeRecv);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//
//                    for (DataSnapshot walkSnapShot : dataSnapshot.getChildren()) {
//                       Log.i(TAG,walkSnapShot.toString());
//                    }
//                }
//            }
//
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null && walk!=null) {
            try {
                ArrayList<LatLng> arrOfLatLng = walk.getArrOfLatLng();
                mMap.addPolyline(new PolylineOptions()
                        .addAll(arrOfLatLng)
                        .width(10)
                        .color(Color.RED));
                mMap.addMarker(new MarkerOptions().position(arrOfLatLng.get(0)).title("Start"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrOfLatLng.get(0),DEFAULT_ZOOM));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
