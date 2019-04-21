package com.example.b00086142.finalproject.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.b00086142.finalproject.Activity.train.TrainHistoryActivity;
import com.example.b00086142.finalproject.Fragment.TimePickerFragment;
import com.example.b00086142.finalproject.R;
import com.example.b00086142.finalproject.bean.HeartRate;
import com.example.b00086142.finalproject.bean.Water;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.formatter.AxisValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;

public class NotifActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "NotifActivity";
    private TextView mTextView;

    LineChartView chart,chartSport,chartRate;

    AlarmManager alarmManager;
    DatabaseReference alertdrf;

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        mTextView = findViewById(R.id.textView);
        chart = findViewById(R.id.chart);
        chartSport = findViewById(R.id.chartSport);
        chartRate = findViewById(R.id.chartRate);
        Button buttonTimePicker = findViewById(R.id.button_timepicker);

        alertdrf =  FirebaseDatabase.getInstance().getReference("userAlert/" + FirebaseAuth.getInstance().getUid() );

        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        Button buttonCancelAlarm = findViewById(R.id.button_cancel);
        buttonCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        findViewById(R.id.button_drink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotifActivity.this,DrinkHistoryActivity.class));
            }
        });
        findViewById(R.id.button_Train).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotifActivity.this,TrainHistoryActivity.class));
            }
        });
        initChart();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alertdrf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        Long time = Long.valueOf(dataSnapshot.getValue().toString());
                        if (time > System.currentTimeMillis()) {
                            String timeText = "Alarm set for: " + DateFormat.getTimeInstance(DateFormat.SHORT).format(time);
                            mTextView.setText(timeText);
                        } else {
                            mTextView.setText("No Alarm set");
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mTextView.setText("No Alarm set");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initChart() {
        chart.setInteractive(true);
        chart.setZoomType(ZoomType.HORIZONTAL);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chartSport.setInteractive(true);
        chartSport.setZoomType(ZoomType.HORIZONTAL);
        chartSport.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chartRate.setInteractive(true);
        chartRate.setZoomType(ZoomType.HORIZONTAL);
        chartRate.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);


        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        final DatabaseReference waters = fd.getReference("waters/"+FirebaseAuth.getInstance().getUid());

        waters.orderByValue().limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PointValue> pointValues = new ArrayList<PointValue>();
                List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
                int i=0,max=-1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Water water =  snapshot.getValue(Water.class);
                    mAxisXValues.add(new AxisValue(i).setLabel(water.getTime()));
                    pointValues.add(new PointValue(i, water.getHasDrink()));
                    max = max <  water.getHasDrink()? water.getHasDrink():max;
                    i++;
                }


                //In most cased you can call data model methods in builder-pattern-like manner.
                Line line = new Line(pointValues).setColor(Color.parseColor("#00CFFF")).setCubic(true).setStrokeWidth(5);
                line.setHasLabels(true);
                List<Line> lines = new ArrayList<Line>();
                lines.add(line);

                LineChartData data = new LineChartData(lines);
                Axis axisX = new Axis();
                axisX.setHasTiltedLabels(true);
                axisX.setTextSize(8);//设置字体大小
                axisX.setMaxLabelChars(8);//最多几个X轴坐标
                axisX.setValues(mAxisXValues);
                axisX.setName("Drink History");
                data.setAxisXBottom(axisX);

                Axis axisY = new Axis();
                axisY.setName("/ml");
                axisY.setTextSize(8);
                axisY.setHasLines(true);
                axisY.setFormatter(new SimpleAxisValueFormatter(0));
                data.setAxisYLeft(axisY);

                chart.setLineChartData(data);
                Viewport v = new Viewport(chart.getMaximumViewport());
                v.bottom=0;
                v.top=max+500;
                chart.setMaximumViewport(v);
                chart.setCurrentViewport(v);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference sportDatabaseReference = fd.getReference("userSport/" + FirebaseAuth.getInstance().getUid());
        sportDatabaseReference.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PointValue> pointValues = new ArrayList<PointValue>();
                List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
                int i=0;
                long max=-1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i(TAG, "onDataChange: "+snapshot.getKey());
                    Log.i(TAG, "onDataChange: "+snapshot.getChildrenCount());
                    mAxisXValues.add(new AxisValue(i).setLabel(snapshot.getKey()));
                    pointValues.add(new PointValue(i, snapshot.getChildrenCount()));
                    max = max < snapshot.getChildrenCount()?snapshot.getChildrenCount():max;
                    i++;
                }


                //In most cased you can call data model methods in builder-pattern-like manner.
                Line line = new Line(pointValues).setColor(Color.YELLOW).setCubic(true).setStrokeWidth(4);
                line.setHasLabels(true);
                List<Line> lines = new ArrayList<Line>();
                lines.add(line);
                LineChartData data = new LineChartData(lines);
                Axis axisX = new Axis();
                axisX.setHasTiltedLabels(true);
                axisX.setTextSize(8);//设置字体大小
                axisX.setMaxLabelChars(8);//最多几个X轴坐标
                axisX.setValues(mAxisXValues);
                axisX.setName("Train Times");
                data.setAxisXBottom(axisX);

                Axis axisY = new Axis();
                axisY.setFormatter(new SimpleAxisValueFormatter(0));
                axisY.setName("/time");
                axisY.setTextSize(8);
                axisY.setHasLines(false);
                axisY.setMaxLabelChars(6);
                data.setAxisYLeft(axisY);

                chartSport.setLineChartData(data);
                Viewport v = new Viewport(chartSport.getMaximumViewport());
                v.bottom=0;
                v.top=max+8;
                chartSport.setMaximumViewport(v);
                chartSport.setCurrentViewport(v);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        DatabaseReference h = fd.getReference("userHeart1/" + FirebaseAuth.getInstance().getUid());
//        h.removeValue();
        DatabaseReference rateDatabaseReference = fd.getReference("userHeart/" + FirebaseAuth.getInstance().getUid());
        rateDatabaseReference.orderByValue().limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PointValue> pointValues = new ArrayList<PointValue>();
                List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
                int i=0;
                long max=-1;
                Log.i(TAG, "heartRate: "+dataSnapshot);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HeartRate heartRate = snapshot.getValue(HeartRate.class);
                    Log.i(TAG, "heartRate: "+heartRate.getTime());
                    Log.i(TAG, "heartRate: "+heartRate.getRate());
//                    DatabaseReference push = h.push();
//                    push.setValue(heartRate);
                    mAxisXValues.add(new AxisValue(i).setLabel(sdf.format(new Date(heartRate.getTime()))));
                    pointValues.add(new PointValue(i, heartRate.getRate()));
                    max = max < heartRate.getRate()?heartRate.getRate():max;
                    i++;
                }


                //In most cased you can call data model methods in builder-pattern-like manner.
                Line line = new Line(pointValues).setColor(Color.RED).setCubic(true).setStrokeWidth(4);
                line.setHasLabels(true);
                List<Line> lines = new ArrayList<Line>();
                lines.add(line);
                LineChartData data = new LineChartData(lines);
                Axis axisX = new Axis();
                axisX.setHasTiltedLabels(true);
                axisX.setTextSize(8);//设置字体大小
                axisX.setMaxLabelChars(8);//最多几个X轴坐标
                axisX.setValues(mAxisXValues);
                axisX.setName("Heart Rate");
                data.setAxisXBottom(axisX);

                Axis axisY = new Axis();
                axisY.setFormatter(new SimpleAxisValueFormatter(0));
                axisY.setName("/rate");
                axisY.setTextSize(8);
                axisY.setHasLines(false);
                axisY.setMaxLabelChars(6);
                data.setAxisYLeft(axisY);

                chartRate.setLineChartData(data);
                Viewport v = new Viewport(chartRate.getMaximumViewport());
                v.bottom=40;
                v.top=160;
                chartRate.setMaximumViewport(v);
                chartRate.setCurrentViewport(v);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
        startAlarm(c);
    }

    private void updateTimeText(Calendar c) {
        String timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        alertdrf.setValue( c.getTime().getTime());
        mTextView.setText(timeText);
    }

    private void startAlarm(Calendar c) {
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
        mTextView.setText("Alarm canceled");
    }
}