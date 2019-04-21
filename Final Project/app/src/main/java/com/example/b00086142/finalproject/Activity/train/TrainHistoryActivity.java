package com.example.b00086142.finalproject.Activity.train;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.b00086142.finalproject.Activity.DrinkHistoryActivity;
import com.example.b00086142.finalproject.R;
import com.example.b00086142.finalproject.bean.Water;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TrainHistoryActivity extends AppCompatActivity {
    private static final String TAG = "TrainHistoryActivity";

    ProgressBar progressBar;
    ListView listview;
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_history);
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference sportDatabaseReference = fd.getReference("userSport/" + FirebaseAuth.getInstance().getUid());

        listview = findViewById(R.id.listview);
        progressBar = findViewById(R.id.progressBar);
        sportDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i(TAG, "onDataChange: "+snapshot.getKey());
                    Log.i(TAG, "onDataChange: "+snapshot.getChildrenCount());

                    list.add(snapshot.getKey()+" exercise completed "+snapshot.getChildrenCount()+" times.");
                }
                listview.setAdapter(new ArrayAdapter<>(TrainHistoryActivity.this,android.R.layout.simple_list_item_1, list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
