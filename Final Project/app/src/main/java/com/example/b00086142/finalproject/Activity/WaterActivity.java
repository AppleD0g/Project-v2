package com.example.b00086142.finalproject.Activity;

import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00086142.finalproject.R;
import com.example.b00086142.finalproject.View.WaterView;
import com.example.b00086142.finalproject.bean.Water;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WaterActivity extends AppCompatActivity {
    private static final String TAG = "WaterActivity";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    private WaterView mWaterView;
    private Button mDrinkButton;
    private int mCurPercent;
    private int mTargetPercent;
    private TextView mCurPercentView;


    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        progressBar = findViewById(R.id.progressBar);
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        final DatabaseReference waters = fd.getReference("waters/"+FirebaseAuth.getInstance().getUid());
/*        btn=(Button)findViewById(R.id.btn_goback);


        //close current activity and go to home activity
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(WaterActivity.this, HomeActivity.class);

                startActivity(intent);
                WaterActivity.this.finish();
            }
        });*/

        mWaterView = (WaterView) findViewById(R.id.waterView);
        mCurPercentView = (TextView) findViewById(R.id.textview_percent);
        mDrinkButton = (Button) findViewById(R.id.button_drink);

        mTargetPercent = 0;
        setPercent(mTargetPercent);

        mWaterView.setFraction(mCurPercent / 2000f);

        mDrinkButton.setOnClickListener(new View.OnClickListener() {
            private ObjectAnimator animator;

            @Override
            public void onClick(View view) {

                if (animator != null) {
                    animator.cancel();
                    animator = null;
                    mCurPercent = mTargetPercent;
                }

//                if ((mCurPercent + 500) > 2000) {
//
//                    animator = ObjectAnimator.ofInt(WaterActivity.this, "percent", mCurPercent, 0);
//                    animator.setDuration(300);
//                    animator.start();
//                    mTargetPercent = 0;
//                    mCurPercent = mTargetPercent;
//                    mWaterView.updateFraction(mCurPercent);
//
//                    return;
//                }

                mTargetPercent = mTargetPercent + 500;
                animator = ObjectAnimator.ofInt(WaterActivity.this, "percent", mCurPercent, mTargetPercent);
                animator.setDuration(300);
                animator.start();

                mCurPercent = mTargetPercent;
                mWaterView.updateFraction(mCurPercent / 2000f);
                if (mCurPercent == 2000) {
                    Toast.makeText(WaterActivity.this, "恭喜达到每日喝水目标", Toast.LENGTH_SHORT).show();
                }
                DatabaseReference push = waters.push();
                push.setValue(new Water(sdf.format(new Date()),mCurPercent));
            }
        });
        waters.orderByValue().limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.e(TAG, "onDataChange: " + snapshot.getValue());
                    Water water =  snapshot.getValue(Water.class);
                    if (water.getTime().split(" ")[0].equals(sdf2.format(new Date()))) {
                        mCurPercent = water.getHasDrink();
                        if (mCurPercent==2000) {
                            Toast.makeText(WaterActivity.this, "恭喜达到每日喝水目标", Toast.LENGTH_SHORT).show();
                        }
                        mTargetPercent = mCurPercent;
                        mWaterView.updateFraction(mCurPercent / 2000f);
                        setPercent(mCurPercent);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(WaterActivity.this, "ERROR", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void setPercent(int percent) {
        mCurPercent = percent;
        mCurPercentView.setText(String.valueOf(percent));
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDrinkButton.post(new Runnable() {
            @Override
            public void run() {
                mWaterView.setBubbleStartPoint(new Point((int) (mDrinkButton.getLeft() + mDrinkButton.getMeasuredWidth() / 2), (int) (mDrinkButton.getTop() + mDrinkButton.getMeasuredHeight() / 2)));
            }
        });

        mWaterView.startAnimation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mWaterView.stopAnimation();
    }
}









