package com.example.b00086142.finalproject.Activity.train;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00086142.finalproject.Activity.HomeActivity;
import com.example.b00086142.finalproject.R;
import com.example.b00086142.finalproject.bean.Sport;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Step2Activity extends AppCompatActivity {
    private static final String TAG = "Step2Activity";
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    Sport sport;
    TextView time,name;
    ImageView res1;

    float rawX1,rawY1,rawX2,rawY2;

    String startTime;
    boolean isStart = true;

    Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);
        intent = getIntent();
        sport = (Sport) intent.getSerializableExtra("sport");
        setTitle(sport.getSname());
        name = findViewById(R.id.name);
        name.setText(sport.getSname());
        time = findViewById(R.id.time);
        Date date = new Date();
        startTime = sdf.format(date);
        intent.putExtra("startTime",date.getTime());
        res1 = findViewById(R.id.res1);
        res1.setImageResource(sport.getSportRes());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isStart) {
                    try {
                        Thread.sleep(1000);
                        time.post(new Runnable() {
                            @Override
                            public void run() {
                                time.setText("Begin:"+startTime+" continue:"+sdf.format(new Date()));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        rawX1 = motionEvent.getRawX();
                        rawY1 = motionEvent.getRawY();
                        Log.i(TAG, "onTouch: "+rawX1+"-"+rawY1);
                        break;
                    case MotionEvent.ACTION_UP:
                        rawX2 = motionEvent.getRawX();
                        rawY2 = motionEvent.getRawY();
                        Log.i(TAG, "onTouch: "+rawX2+"-"+rawY2);
                        if (Math.abs(rawX1 - rawX2) > 80 || Math.abs(rawY1 - rawY2) > 80) {
                            //Toast.makeText(Step2Activity.this, "Congratulations on completing "+sport.getSname()+", "+8+" times", Toast.LENGTH_SHORT).show();
                            isStart = false;
                            intent.setClass(Step2Activity.this,Step3Activity.class);
                            intent.putExtra("endTime",new Date().getTime());
                            startActivity(intent);
                            finish();
                        }

                        break;
                }
                return false;
            }
        });

    }
}
