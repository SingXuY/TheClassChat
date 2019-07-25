package com.example.classchat.Activity;

//搜索添加课程界面

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import com.example.classchat.Adapter.Adapter_SearchCourseListView;
import com.example.classchat.Util.SharedUtil;
import com.example.classchat.model.AddCourseDataBase;
import com.example.classchat.R;
import java.util.*;


public class Activity_AddSearchCourse extends AppCompatActivity {

    //声明控件
    private EditText edit;
    private ListView list;
    private AlertDialog.Builder builder;
    private Button back;
    private Button add;
    private Context mContext;
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
        setContentView(R.layout.activity__add_search_course);



        //绑定控件
        edit=(EditText)findViewById(R.id.edit_search);
        back=(Button)findViewById(R.id.back_from_searchAddCourse_button);
        add=(Button)findViewById(R.id.add_personally);
        list = (ListView) findViewById(R.id.search_list);
        mContext=this;





        //手动添加提示框；
        builder = new AlertDialog.Builder(this);
        builder.setTitle("提示：");
        builder.setMessage("手动添加的课程可能无法使用唠课的聊天功能哦，是否确定？");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setClass(Activity_AddSearchCourse.this,Activity_AddCourse.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });



      //搜索编辑框的文字监听
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                //todo s.toString()为输入的关键字，这里需要写把关键字发给数据库和返回一个包含(id，课程名，老师，专业)
                // （固定顺序）的二维string数组的函数，数组给hoolder赋值；
                String [][]holder=new String[4][];


               //根据holder内容创建listview
                int size=holder[1].length;
                List<AddCourseDataBase> items=new ArrayList<AddCourseDataBase>();
                for(int i=0;i<size;i++){
                    AddCourseDataBase item=new AddCourseDataBase(holder[1][i],holder[2][i],holder[3][i],holder[4][i]);
                    items.add(item);
                }
                list.setAdapter(new Adapter_SearchCourseListView(mContext,items));
            }
        });



        //返回
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Activity_AddSearchCourse.this,MainActivity.class);
                startActivity(intent);
            }
        });
        //跳转手动添加
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               builder.show();
            }
        });
    }

}


