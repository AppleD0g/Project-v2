package com.example.b00086142.finalproject.Activity.train;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.b00086142.finalproject.R;
import com.example.b00086142.finalproject.bean.Sport;

public class Step1Activity extends AppCompatActivity {

    SeekBar seekBar;
    TextView time;
    ImageView res1;
    Sport sport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1);
        sport = (Sport) getIntent().getSerializableExtra("sport");
        setTitle(sport.getSname());
        seekBar = findViewById(R.id.seekBar);
        res1 = findViewById(R.id.res1);
        time = findViewById(R.id.time);
        time.setText(seekBar.getProgress()+" mins");
        res1.setImageResource(sport.getSportRes());

        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = seekBar.getProgress();
                sport.setTraintime(progress);
                Intent intent = new Intent(Step1Activity.this,Step2Activity.class);
                intent.putExtra("sport",sport);
                finish();
                startActivity(intent);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                time.setText(seekBar.getProgress()+" mins");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
