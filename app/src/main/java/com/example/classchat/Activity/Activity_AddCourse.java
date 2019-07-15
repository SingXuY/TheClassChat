package com.example.classchat.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;

import android.os.Bundle;

import com.example.classchat.R;

import java.time.Instant;

public class Activity_AddCourse extends AppCompatActivity {

    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add_course);
        back=(Button)findViewById(R.id.back_from_addCourse_button);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Activity_AddCourse.this,Activity_Main_Timetable.class);
                startActivity(intent);
            }
        });

    }
}
