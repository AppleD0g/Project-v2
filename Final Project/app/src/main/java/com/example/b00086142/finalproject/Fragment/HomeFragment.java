package com.example.b00086142.finalproject.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.b00086142.finalproject.Activity.FitActivity;
import com.example.b00086142.finalproject.Activity.MapsActivity;
import com.example.b00086142.finalproject.Activity.StepCountActivity;
import com.example.b00086142.finalproject.Activity.WaterActivity;
import com.example.b00086142.finalproject.Activity.train.Step1Activity;
import com.example.b00086142.finalproject.R;
import com.example.b00086142.recorder.activities.MainActivity;


public class HomeFragment extends android.support.v4.app.Fragment implements View.OnClickListener {


    public HomeFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Button paobu = (Button) rootView.findViewById(R.id.but_paobu);
        paobu.setOnClickListener(this);

        Button walk = (Button) rootView.findViewById(R.id.but_walk);
        walk.setOnClickListener(this);

        Button jianshen = (Button) rootView.findViewById(R.id.but_jianshen);
        jianshen.setOnClickListener(this);

        Button water = (Button) rootView.findViewById(R.id.but_water);
        water.setOnClickListener(this);

       rootView.findViewById(R.id.but_Step).setOnClickListener(this);

        paobu.setOnClickListener(this);
        walk.setOnClickListener(this);
        jianshen.setOnClickListener(this);
        water.setOnClickListener(this);

        return rootView;

    }



    @Override
    public void onClick(View view) {

        android.support.v4.app.Fragment fragment = null;
        Intent in =null;

        switch(view.getId()){
            case R.id.but_paobu:
                in = new Intent(getContext(), MainActivity.class);
                startActivity(in);
                break;
            case R.id.but_walk:
                 in = new Intent(getContext(), MapsActivity.class);
                startActivity(in);
                break;
            case R.id.but_jianshen:
                fragment = new TrainFragment();
                replaceFragment(fragment);
                break;
            case R.id.but_water:
                in = new Intent(getContext(), WaterActivity.class);
                startActivity(in);
                //overridePendingTransition(0, 0);
                break;
            case R.id.but_Step:
                in = new Intent(getContext(), StepCountActivity.class);
                startActivity(in);
                //overridePendingTransition(0, 0);
                break;
        }

    }

    private void replaceFragment(android.support.v4.app.Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();



    }

}