package com.example.b00086142.finalproject.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.b00086142.finalproject.Activity.LogOutActivity;
import com.example.b00086142.finalproject.Activity.LoginActivity;
import com.example.b00086142.finalproject.R;
import com.example.b00086142.finalproject.bean.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "ProfileFragment";
    ImageView face;
    EditText username;
    EditText sex;
    EditText birthday;
    EditText height;
    EditText weight;
ProgressBar progressBar;
    FirebaseAuth auth;
    DatabaseReference userprofile;

    private static final int TAKEIMAGE = 20;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Button button = (Button) rootView.findViewById(R.id.button_profile);
        face = rootView.findViewById(R.id.face);
        username = rootView.findViewById(R.id.username);
        sex = rootView.findViewById(R.id.sex);
        birthday = rootView.findViewById(R.id.birthday);
        height = rootView.findViewById(R.id.height);
        weight = rootView.findViewById(R.id.weight);
        progressBar = rootView.findViewById(R.id.progressBar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start your activity here
                Intent i = new Intent(rootView.getContext(), LogOutActivity.class);
                startActivity(i);
            }
        });
        face.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, TAKEIMAGE);
            }
        });
        rootView.findViewById(R.id.button_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = username.getText().toString();
                String s = sex.getText().toString();
                String b = birthday.getText().toString();
                String h = height.getText().toString();
                String w = weight.getText().toString();
                User user = new User(n, s, b, h, w);
                progressBar.setVisibility(View.VISIBLE);
                userprofile.setValue(user);
            }
        });
        rootView.findViewById(R.id.button_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                startActivity(new Intent(getActivity(), LogOutActivity.class));
            }
        });
        initView();
        return rootView;
    }

    private void initView() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(listener);
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        userprofile = fd.getReference("userprofile/" + auth.getUid());
        userprofile.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                Log.i(TAG, "onDataChange: "+auth.getUid()+">>"+u);
                if (u != null) {
                    username.setText(u.getName());
                    sex.setText(u.getSex());
                    birthday.setText(u.getBirthday());
                    height.setText(u.getHeight());
                    weight.setText(u.getWeight());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "FAIL", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
//        userprofile.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User u = dataSnapshot.getValue(User.class);
//                if (u != null) {
//                    Log.i(TAG, "onDataChange: SUCCESS "+auth.getUid()+">>"+u);
//                    Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "FAIL", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    FirebaseAuth.AuthStateListener listener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            Log.i(TAG, "onAuthStateChanged: " + firebaseAuth);
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                face.setImageURI(user.getPhotoUrl());
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKEIMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.i(TAG, "onActivityResult: " + data);
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build();
            face.setImageURI(uri);
            auth.getCurrentUser().updateProfile(userProfileChangeRequest);

        }
    }
}