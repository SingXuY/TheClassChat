package com.example.classchat.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import android.widget.Toast;
import android.util.Log;
import android.os.Bundle;
import android.widget.TextView;
import java.util.*;

import com.example.classchat.R;

import java.time.Instant;

public class Activity_AddCourse extends AppCompatActivity {


    private AlertDialog.Builder mutilChoicebuilder;
    private Button back;
    private TextView choose_week;

    private final String[] weeks= new String[]{"第1周","第2周","第3周","第4周","第5周","第6周","第7周","第8周","第9周","第10周","第11周","第12周","第13周","第14周","第15周","第16周","第17周","第18周","第19周","第20周","第21周","第22周","第23周","第24周","第25周"};
    private boolean[] weeksChecked = new boolean[]{false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add_course);


        back=(Button)findViewById(R.id.back_from_addCourse_button);
        choose_week=(TextView)findViewById(R.id.choose_week);

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Activity_AddCourse.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //多选框
        mutilChoicebuilder = new AlertDialog.Builder(this);
        mutilChoicebuilder.setTitle("选择周数");
        mutilChoicebuilder.setMultiChoiceItems(weeks, weeksChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

            }
        });
        mutilChoicebuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = new String();
                int end = 0;
                List<Integer> weeksnum=new ArrayList<Integer>();
                for(int i=0;i<weeksChecked.length;i++){
                    if(weeksChecked[i])
                    weeksnum.add(i+1);
                }

                for(int i = 0; i < weeksChecked.length; i++)
                {
                    if (weeksChecked[i])
                    {

                        int start = i+1;
                        for(int j=i+1;j<=weeksChecked.length;++j) {
                            if (j == weeksChecked.length && weeksChecked[j - 1]) {
                                end = weeksChecked.length;
                                i = weeksChecked.length - 1;
                                break;
                            } else if (!weeksChecked[j]) {
                                end = j;
                                i = j;
                                break;
                            }
                        }
                        if(start==end)
                            s+="第"+start+"周 ";
                        else
                            s+="第"+start+"~"+end+"周 ";

                    }

                }
                if (weeksnum.size() > 0){
                    choose_week.setText(s);
                }else{
                    //没有选择
                    Toast.makeText(Activity_AddCourse.this, "未选择周数!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        mutilChoicebuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        choose_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mutilChoicebuilder.show();
            }
        });

    }
}
