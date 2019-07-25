package com.example.classchat.Fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classchat.Activity.Activity_AddSearchCourse;
import com.example.classchat.Activity.Activity_ClassDetailInformation;
import com.example.classchat.Activity.MainActivity;
import com.example.classchat.Object.MySubject;
import com.example.classchat.R;
import com.example.classchat.Util.SharedUtil;
import com.example.classchat.Util.ThemeUIUtil;
import com.example.classchat.model.SubjectRepertory;
import com.example.library_activity_timetable.Activity_TimetableView;
import com.example.library_activity_timetable.listener.ISchedule;
import com.example.library_activity_timetable.listener.IWeekView;
import com.example.library_activity_timetable.model.Schedule;
import com.example.library_activity_timetable.view.WeekView;

import java.lang.reflect.Field;
import java.util.List;


public class Fragment_ClassBox extends Fragment implements OnClickListener {



    private static final String TAG = "Activity_Main_Timetable";

    //控件
    Activity_TimetableView mTimetableView;
    WeekView mWeekView;
    private View view;
    ImageButton moreButton;
    LinearLayout layout;
    TextView titleTextView;
    List<MySubject> mySubjects;

    //记录切换的周次，不一定是当前周
    int target = -1;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.activity__main__timetable, container, false);

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        moreButton =(ImageButton)getActivity().findViewById(R.id.id_more);
        moreButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopmenu();
            }
        });
        view=getActivity().findViewById(R.id.activity_main_timetable);
        mySubjects = SubjectRepertory.loadDefaultSubjects2();
        mySubjects.addAll(SubjectRepertory.loadDefaultSubjects());
        titleTextView = (TextView)getActivity().findViewById(R.id.id_title);
        layout = (LinearLayout)getActivity().findViewById(R.id.id_layout);
        layout.setOnClickListener(this);
        initTimetableView();
    }

    /**
     * 初始化课程控件
     */
    private void initTimetableView() {
        //获取控件
        mWeekView = (WeekView)getActivity().findViewById(R.id.id_weekview);
        mTimetableView = (Activity_TimetableView)getActivity().findViewById(R.id.id_timetableView);

        //设置周次选择属性
        mTimetableView.curWeek("2019-07-01 00:00:00");
        mWeekView.source(mySubjects)
                .curWeek(mTimetableView.curWeek())
                .callback(new IWeekView.OnWeekItemClickedListener() {
                    @Override
                    public void onWeekClicked(int week) {
                        int cur = mTimetableView.curWeek();
                        //更新切换后的日期，从当前周cur->切换的周week
                        mTimetableView.onDateBuildListener()
                                .onUpdateDate(cur, week);
                        mTimetableView.changeWeekOnly(week);
                    }
                })
                .callback(new IWeekView.OnWeekLeftClickedListener() {
                    @Override
                    public void onWeekLeftClicked() {
                        onWeekLeftLayoutClicked();
                    }
                })
                .isShow(false)//设置隐藏，默认显示
                .showView();

        mTimetableView.source(mySubjects)
//                .curWeek("2019-07-10")
                .curTerm("大三下学期")
                .maxSlideItem(12)
                .monthWidthDp(30)

                .callback(new ISchedule.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, Schedule schedule) {
                        display(schedule);
                    }
                })
                .callback(new ISchedule.OnItemLongClickListener() {
                    @Override
                    public void onLongClick(View v, int day, int start) {
                        Toast.makeText(getActivity(),
                                "长按:周" + day  + ",第" + start + "节",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .callback(new ISchedule.OnWeekChangedListener() {
                    @Override
                    public void onWeekChanged(int curWeek) {
                        titleTextView.setText("第" + curWeek + "周");
                    }
                })
                //旗标布局点击监听
                .callback(new ISchedule.OnFlaglayoutClickListener() {
                    @Override
                    public void onFlaglayoutClick(int day, int start) {
                        mTimetableView.hideFlaglayout();
                        Toast.makeText(getActivity(),
                                "点击了旗标:周" + (day + 1) + ",第" + start + "节",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .showView();
        hideNonThisWeek();
    }

    /**
     * 更新一下，防止因程序在后台时间过长（超过一天）而导致的日期或高亮不准确问题。
     */
    @Override
    public void onStart() {
        super.onStart();
        mTimetableView.onDateBuildListener()
                .onHighLight();
    }

    /**
     * 周次选择布局的左侧被点击时回调<br/>
     * 对话框修改当前周次
     */
    protected void onWeekLeftLayoutClicked() {
        final String items[] = new String[25];
        int itemCount = mWeekView.itemCount();
        for (int i = 0; i < itemCount; i++) {
            items[i] = "第" + (i + 1) + "周";
        }
        target = -1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("设置当前周");
        builder.setSingleChoiceItems(items, mTimetableView.curWeek() - 1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        target = i;
                    }
                });
        builder.setPositiveButton("设置为当前周", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (target != -1) {
                    mWeekView.curWeek(target + 1).updateView();
                    mTimetableView.changeWeekForce(target + 1);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     *
     * @param bean
     */
    protected void display(Schedule bean) {
/*
        Toast.makeText(getActivity(),
                bean.getName(),
                Toast.LENGTH_SHORT).show();
 */
        Intent coursedetail=new Intent(getActivity(), Activity_ClassDetailInformation.class);
        coursedetail.putExtra("coursename",bean.getName());
        coursedetail.putExtra("courseday",bean.getDay());
        coursedetail.putExtra("courseplace",bean.getRoom());
        coursedetail.putExtra("coursestart",bean.getStart());
        coursedetail.putExtra("coursestep",bean.getStep());
        coursedetail.putExtra("courseteacher",bean.getTeacher());
        startActivity(coursedetail);

    }

    /**
     * 显示弹出菜单
     */
    @SuppressLint("RestrictedApi")
    public void showPopmenu() {
        PopupMenu popup = new PopupMenu(this.getActivity(), moreButton);
        popup.getMenuInflater().inflate(R.menu.tabletime_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_add_course:
                        addSubject();
                        break;
                    case R.id.menu_shownotthisweek:
                        showNonThisWeek();
                        break;

                    case R.id.menu_hidenotthisweek:
                        hideNonThisWeek();
                        break;
                    case R.id.menu_showweekend:
                        showWeekends();
                        break;
                    case R.id.menu_hideweekend:
                        hideWeekends();
                        break;
                    case R.id.menu_changestyle:
                        changstyle();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        try { //popupmenu显示icon的关键
            Field mpopup=popup.getClass().getDeclaredField("mPopup");
            mpopup.setAccessible(true);
            MenuPopupHelper mPopup = (MenuPopupHelper) mpopup.get(popup);
            mPopup.setForceShowIcon(true);
        } catch (Exception e) {
        }
        popup.show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.id_layout:
                //如果周次选择已经显示了，那么将它隐藏，更新课程、日期
                //否则，显示
                if (mWeekView.isShowing()) {
                    mWeekView.isShow(false);
                    titleTextView.setTextColor(getResources().getColor(R.color.app_black));
                    int cur = mTimetableView.curWeek();
                    mTimetableView.onDateBuildListener()
                            .onUpdateDate(cur, cur);
                    mTimetableView.changeWeekOnly(cur);
                } else {
                    mWeekView.isShow(true);
                    titleTextView.setTextColor(getResources().getColor(R.color.app_gold));
                }
                break;
        }
    }

    /**
     * 删除课程
     * 内部使用集合维护课程数据，操作集合的方法来操作它即可
     * 最后更新一下视图（全局更新）
     */
    protected void deleteSubject() {
        int size = mTimetableView.dataSource().size();
        int pos = (int) (Math.random() * size);
        if (size > 0) {
            mTimetableView.dataSource().remove(pos);
            mTimetableView.updateView();
        }
    }

    /**
     * 添加课程
     * 内部使用集合维护课程数据，操作集合的方法来操作它即可
     * 最后更新一下视图（全局更新）
     */
    protected void addSubject() {
        Intent add = new Intent(getActivity(), Activity_AddSearchCourse.class);
        startActivity(add);
    }

    /**
     * 隐藏非本周课程
     * 修改了内容的显示，所以必须更新全部（性能不高）
     * 建议：在初始化时设置该属性
     * <p>
     * updateView()被调用后，会重新构建课程，课程会回到当前周
     */
    protected void hideNonThisWeek() {
        mTimetableView.isShowNotCurWeek(false).updateView();
        mWeekView.curWeek(mTimetableView.curWeek()).updateView();
        mTimetableView.changeWeekForce(mTimetableView.curWeek());
    }

    /**
     * 显示非本周课程
     * 修改了内容的显示，所以必须更新全部（性能不高）
     * 建议：在初始化时设置该属性
     */
    protected void showNonThisWeek() {
        mTimetableView.isShowNotCurWeek(true).updateView();
        mWeekView.curWeek(mTimetableView.curWeek()).updateView();
        mTimetableView.changeWeekForce(mTimetableView.curWeek());
    }

    /**
     * 显示WeekView
     */

    protected void showWeekView() {
        mWeekView.isShow(true);
    }

    /**
     * 隐藏WeekView
     */

    protected void hideWeekView() {
        mWeekView.isShow(false);
    }


    /**
     * 隐藏周末
     */
    private void hideWeekends() {
        mTimetableView.isShowWeekends(false).updateView();
    }

    /**
     * 显示周末
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showWeekends() {
        mTimetableView.isShowWeekends(true).updateView();


    }
    private void changstyle(){
        //我们先取这个根布局的 bitmap缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        final Bitmap localBitmap=Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        if(SharedUtil.getShartData(getActivity(),"name").equals("night")){
            SharedUtil.setShartData(getActivity(),"day");

//                    recreate();
            getActivity().setTheme(R.style.dayTheme);
            getActivity().recreate();

        }else{
            SharedUtil.setShartData(getActivity(),"night");
//                    recreate();
            getActivity().setTheme(R.style.nightTheme);
            getActivity().recreate();

        }

    }


    }

