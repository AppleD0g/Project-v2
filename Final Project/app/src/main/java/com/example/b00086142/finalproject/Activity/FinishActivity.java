package com.example.b00086142.finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.b00086142.finalproject.R;

public class FinishActivity extends AppCompatActivity {
    private String INTENT_DISTANCEKEY = "distance";
    private String INTENT_TIMEKEY = "time";
    private String INTENT_SPEED = "speed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        setTitle(R.string.runCompleted);

        Intent intent = getIntent();
        double completedDist = intent.getExtras().getDouble(INTENT_DISTANCEKEY);
        double speed = intent.getExtras().getDouble(INTENT_SPEED);
        long completedTime = intent.getExtras().getLong(INTENT_TIMEKEY);


        TextView textViewDurationF = (TextView) findViewById(R.id.textViewTimeF);
        TextView textViewDistanceF = (TextView) findViewById(R.id.textViewDistF);
        TextView textViewDistanceS = (TextView) findViewById(R.id.textViewDistS);

        Button buttonBack = (Button) findViewById(R.id.buttonBack);

        textViewDistanceF.setText(formatDistance(completedDist));
        textViewDurationF.setText(formatDuration(completedTime));
        textViewDistanceS.setText("Speed " + String.format("%.2f",speed) +" km/h");

        buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentToMap = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intentToMap);
                finish();
            }
        });


    }


    public String formatDistance(double pDistance) {
        if (pDistance / 1000 >= 1) {
            String distanceStr = String.format("%.2f", (pDistance / 1000));
            return distanceStr + "km";
        } else {
            String distanceStr = String.format("%.0f", pDistance);
            return distanceStr + "m";
        }
    }

    public String formatDuration(long pDuration) {
        return DateUtils.formatElapsedTime(pDuration);

    }
}
