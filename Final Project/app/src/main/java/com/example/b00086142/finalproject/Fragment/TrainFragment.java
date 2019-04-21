package com.example.b00086142.finalproject.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import com.example.b00086142.finalproject.Activity.train.SportActivity;
import com.example.b00086142.finalproject.R;
import com.example.b00086142.finalproject.bean.Sport;


public class TrainFragment extends Fragment implements View.OnClickListener {

    List<Sport> sports = new ArrayList<>();

    {
        Sport s1 = new Sport("Running");
        s1.setIntroduce("Running is a popular kind of exercise. Because it can bring us many advantages. First it can build up our strength and make us healthy. Second it can get rid of our pressure and make us relaxed. Third it can prevent diseases and make us keep fit. Finally it can also exercise our patience. In a word ,running is a wonderful form of exercise. If you are involved in it ,you can get more pleasure.");
        s1.setSportRes(R.mipmap.running);
        sports.add(s1);

        s1 = new Sport("Sit-up");
        s1.setIntroduce(                "Seabees conduct their sit-up portion of the Navy Physical Readiness Test\n" +
                "The sit-up (or curl-up) is an abdominal endurance training exercise to strengthen and tone the abdominal muscles. It is similar to a crunch (crunches target the rectus abdominis and also work the external and internal obliques), but sit-ups have a fuller range of motion and condition additional muscles.");
        s1.setSportRes(R.mipmap.sit_up);
        sports.add(s1);

        s1 = new Sport("push-up");
        s1.setIntroduce("\n" +
                "Animation of a full push-up (the wide positioning of the hands increases the push-up's use of chest muscles as opposed to arm muscles)\n" +
                "\n" +
                "Side view of a push-up\n" +
                "File:Navy-seal-buds-training-push-ups.ogv\n" +
                "Push-up technique\n" +
                "A push-up (or press-up) is a common calisthenics exercise beginning from the prone position, or the front leaning rest position known in the military. By raising and lowering the body using the arms, push-ups exercise the pectoral muscles, triceps, and anterior deltoids, with ancillary benefits to the rest of the deltoids, serratus anterior, coracobrachialis and the midsection as a whole. Push-ups are a basic exercise used in civilian athletic training or physical education and commonly in military physical training. They are also a common form of punishment used in the military, school sport, or in some martial arts disciplines.");
        s1.setSportRes(R.mipmap.push_up);
        sports.add(s1);

        s1 = new Sport("pull-up");
        s1.setIntroduce("File:Navy-seal-buds-training-pull-ups.ogv\n" +
                "pull-up techniques\n" +
                "A pull-up is an upper-body compound pulling exercise. Although it can be performed with any grip, in recent years some have used the term to refer more specifically to a pull-up performed with a palms-forward position.\n" +
                "\n" +
                "The term chin-up, traditionally referring to a pull-up with the chin brought over the top of a bar, was used in the 1980s to refer to a palms-away (overhand/pronated) grip, with a palms-toward (underhand/supinated) grip being called a \"reverse-grip\" chin-up. [1][2][3][4]\n" +
                "\n" +
                "In later decades, this usage has inverted, with some using \"chin\" to refer to a pull-up done with a palms-backward position. In spite of this, \"chin\" is still regularly used refer to overhand-grip");
        s1.setSportRes(R.mipmap.pull_up);
        sports.add(s1);

        s1 = new Sport("dumbbell");
        s1.setIntroduce("The forerunner of the dumbbell, halteres, were used in ancient Greece as lifting weights and also as weights in the ancient Greek version of the long jump. A kind of dumbbell was also used in India for more than a millennium, shaped like a club – so it was named Indian club.The design of the \"Nal\", as the equipment was referred to, can be seen as a halfway point between a barbell and a dumbbell. It was generally used in pairs, in workouts by wrestlers, bodybuilders, sports players, and others wishing to increase strength and muscle size.");
        s1.setSportRes(R.mipmap.dumbbell);
        sports.add(s1);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_train, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ViewGroup group = view.findViewById(R.id.content);
        for (int i = 0; i < group.getChildCount(); i++) {
            Button button = (Button) group.getChildAt(i);
            final Sport sport = sports.get(i);
            button.setText(sport.getSname());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), SportActivity.class);
                    intent.putExtra("sport", sport);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {

    }
}
