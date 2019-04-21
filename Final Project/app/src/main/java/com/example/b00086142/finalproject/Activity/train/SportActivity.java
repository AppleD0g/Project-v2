package com.example.b00086142.finalproject.Activity.train;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.b00086142.finalproject.R;
import com.example.b00086142.finalproject.bean.Sport;

public class SportActivity extends AppCompatActivity {

    TextView introduce;
    ImageView res1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        Sport sport = (Sport) getIntent().getSerializableExtra("sport");
        setTitle(sport.getSname());
        introduce = findViewById(R.id.introduce);
        res1 = findViewById(R.id.res1);
        introduce.setText(sport.getIntroduce());
        res1.setImageResource(sport.getSportRes());

        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent().setClass(SportActivity.this, Step1Activity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
