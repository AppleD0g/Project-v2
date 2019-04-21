package com.example.b00086142.finalproject.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

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

public class DrinkHistoryActivity extends AppCompatActivity {
    ProgressBar progressBar;
    ListView listview;
    List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_history);
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        final DatabaseReference waters = fd.getReference("waters/"+FirebaseAuth.getInstance().getUid());
//        waters.removeValue();
        listview = findViewById(R.id.listview);
        progressBar = findViewById(R.id.progressBar);
        waters.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Water water =  snapshot.getValue(Water.class);
                    list.add(water.toString());
                }
                listview.setAdapter(new ArrayAdapter<>(DrinkHistoryActivity.this,android.R.layout.simple_list_item_1, list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
