package com.example.b00086142.finalproject.Activity.train;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.b00086142.finalproject.Activity.HomeActivity;
import com.example.b00086142.finalproject.R;
import com.example.b00086142.finalproject.bean.Sport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Step3Activity extends AppCompatActivity {

    TextView name, time, intr;
    ImageView res1;

    FirebaseDatabase fd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);
        Sport sport = (Sport) getIntent().getSerializableExtra("sport");
        long startTime = getIntent().getLongExtra("startTime", 0);
        long endTime = getIntent().getLongExtra("endTime", 0);
        sport.setTraintime(endTime - startTime);

        name = findViewById(R.id.name);
        time = findViewById(R.id.time);
        intr = findViewById(R.id.intr);
        res1 = findViewById(R.id.res1);

        res1.setImageResource(sport.getSportRes());
        name.setText("Congratulations on completing " + sport.getSname());

        fd = FirebaseDatabase.getInstance();
        DatabaseReference sportDatabaseReference = fd.getReference("userSport/" + FirebaseAuth.getInstance().getUid()+ "/" + sport.getSname() );
        sportDatabaseReference.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                time.setText("You have train " + dataSnapshot.getChildrenCount() + " times");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference push = sportDatabaseReference.push();
        sport.setIntroduce("");
        push.setValue(sport);
        intr.setText("This train time : " + sport.getTraintime() / 1000 / 60 + " mins \n" +
                "Consumed kilocalorie :" + 5 * sport.getTraintime() / 1000 / 60 + " kcal");

        findViewById(R.id.button_back).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                startActivity(new Intent(Step3Activity.this, HomeActivity.class));
                return false;
            }
        });

    }
}
