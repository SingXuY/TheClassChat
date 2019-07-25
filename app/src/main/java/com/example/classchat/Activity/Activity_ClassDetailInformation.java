package com.example.classchat.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.classchat.Object.MySubject;
import com.example.classchat.R;
import com.example.classchat.Util.SharedUtil;
import com.example.classchat.model.SubjectRepertory;
import com.example.library_activity_timetable.listener.ICourseDetailMenuView;
import com.example.library_activity_timetable.view.CourseDetailView;

import java.util.List;

/**
 * 这是课程详情界面
 */
public class Activity_ClassDetailInformation extends AppCompatActivity  {

    private static final String TAG = "ClassDetailInformationActivity";

    //控件
    CourseDetailView mCourseDetailView;
    List<MySubject> mySubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(SharedUtil.getShartData(this,"name").equals("0")){
            SharedUtil.setShartData(this,"day");
        }
        if(SharedUtil.getShartData(this,"name").equals("night")){
            //设置夜晚主题  需要在setContentView之前
            setTheme(R.style.nightTheme);
        }else{
            //设置白天主题
            setTheme(R.style.dayTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__class_detail_information);

        mySubjects = SubjectRepertory.loadDefaultSubjects2();
        mySubjects.addAll(SubjectRepertory.loadDefaultSubjects());
        initCourseDetailMenu();
    }

    private void initCourseDetailMenu(){
        mCourseDetailView=findViewById(R.id.id_coursemenu);
        mCourseDetailView.source(mySubjects)
                .courseCount(10)
                .callback(new ICourseDetailMenuView.OnCourseDetailMenuItemClickedListener() {
                    @Override
                    public void onCourseDetailMenuClicked(int course) {
                        onCourseDetailClicked(course);
                    }
                })
                .showView();

        TextView name=findViewById(R.id.id_coursename);
        name.setText(getIntent().getExtras().getString("coursename"));
        TextView place=findViewById(R.id.id_courseplace);
        place.setText("教室："+getIntent().getExtras().getString("courseplace"));
        TextView time=findViewById(R.id.id_coursetime);
        time.setText("时间：周"+getIntent().getExtras().getInt("courseday")+"第"+getIntent().getExtras().getInt("coursestart")+"-"+(getIntent().getExtras().getInt("coursestart")+getIntent().getExtras().getInt("coursestep")-1)+"节");
        TextView teacher=findViewById(R.id.id_courseteacher);
        teacher.setText("教师："+getIntent().getExtras().getString("courseteacher"));

    }

    protected void onCourseDetailClicked(int course) {

        TextView name=findViewById(R.id.id_coursename);
        name.setText(mySubjects.get(course).getName());
        TextView place=findViewById(R.id.id_courseplace);
        place.setText("教室："+mySubjects.get(course).getRoom());
        TextView time=findViewById(R.id.id_coursetime);
        time.setText("时间：周"+mySubjects.get(course).getDay()+"第"+mySubjects.get(course).getStart()+"-"+(mySubjects.get(course).getStart()+mySubjects.get(course).getStep()-1)+"节");
        TextView teacher=findViewById(R.id.id_courseteacher);
        teacher.setText("教师："+mySubjects.get(course).getTeacher());



    }

}
