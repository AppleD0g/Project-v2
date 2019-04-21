package com.example.b00086142.finalproject;

import android.content.Context;
import android.content.Intent;

import com.example.b00086142.finalproject.Activity.StepCountActivity;
import com.today.step.lib.BaseClickBroadcast;


public class MyReceiver extends BaseClickBroadcast {

    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        TSApplication tsApplication = (TSApplication) context.getApplicationContext();
        if (!tsApplication.isForeground()) {
            Intent mainIntent = new Intent(context, StepCountActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        } else {

        }
    }
}
