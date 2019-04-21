package com.example.b00086142.finalproject.Activity;

/**
 * Retrieve all the walks that are stored in the database and display
 * a listview which on click, will start another activity based on the walk selected.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.b00086142.finalproject.R;
import com.example.b00086142.finalproject.bean.Walk;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfWalks extends AppCompatActivity {

    ListView listview;
    ArrayList<Walk> list = new ArrayList<>();
    private String TAG = "walking";
    Intent intent;
    private String INTENT_DATETIMEKEY = "dateTime";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_walks);
        setTitle(R.string.listOfWalks);

        //create the view elements
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listview = (ListView) findViewById(R.id.listview);

        final ArrayAdapter<Walk> adapter = new ArrayAdapter<Walk>(this, android.R.layout.simple_dropdown_item_1line, list);
        listview.setAdapter(adapter);
        intent = new Intent(this, IndividualWalkActivity.class);

        //before the retrieve of data from database, show the circular progress bar.
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rt = database.getReference("user1/walk");
        rt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //  retrieve the dateTime of all the walks in the database and display them on the listview
                // once data has been fetch, the progress bar is removed.
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.i(TAG, "onDataChange: "+postSnapshot);
                    HashMap<String,Object> value = (HashMap<String, Object>) postSnapshot.getValue();
                    Walk walk = new Walk();
                    walk.setDuration(Long.valueOf(value.get("duration").toString()));
                    walk.setDistance(Double.valueOf(value.get("distance").toString()));
//                    ArrayList<LatLng> arrOfLatLng = new ArrayList<>();
//                    for (Map<String,Double> m : (ArrayList<Map<String,Double>>) value.get("arrOfLatLng")) {
//                        Double latitude = m.get("latitude");
//                        Double longitude = m.get("longitude");
//                        arrOfLatLng.add(new LatLng(latitude,longitude));
//                    }

                    walk.setArrOfLatLng((ArrayList) value.get("arrOfLatLng"));
                    walk.setTime(String.valueOf(value.get("time")));
//                    Walk walk = postSnapshot.getValue(Walk.class);
                    list.add(walk);
                }
                Collections.reverse(list);
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // when an item of the list is selected, pass info of the selected walk to the next activity.

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Walk walk =(Walk) parent.getItemAtPosition(position);
                intent.putExtra(INTENT_DATETIMEKEY, walk);
                startActivity(intent);

            }
        });
    }
}
