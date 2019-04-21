package com.example.b00086142.finalproject.Fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.b00086142.finalproject.R;

public class NotificationFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notif, container, false);

        Button button = (Button) view.findViewById(R.id.button_timepicker);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getActivity().getSupportFragmentManager(), "time picker");

            }
        });
        return view;
    }





        @Override
        public void onTimeSet (TimePicker view,int hourOfDay, int minute){
            TextView textView = (TextView)view.findViewById(R.id.textView);
            textView.setText("Hour: " + hourOfDay+ "Minute: " + minute );
        }
    }


