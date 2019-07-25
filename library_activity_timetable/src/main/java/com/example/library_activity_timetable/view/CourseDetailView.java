package com.example.library_activity_timetable.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.library_activity_timetable.R;
import com.example.library_activity_timetable.listener.ICourseDetailMenuView;
import com.example.library_activity_timetable.listener.OnCourseDetailMenuItemClickedAdapter;
import com.example.library_activity_timetable.model.CourseDetailViewEnable;
import com.example.library_activity_timetable.model.Schedule;
import com.example.library_activity_timetable.model.ScheduleEnable;
import com.example.library_activity_timetable.model.ScheduleSupport;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailView extends LinearLayout  implements CourseDetailViewEnable<CourseDetailView> {

    private static final String TAG = "CourseDetailView";
    LayoutInflater mInflate;

    //根布局
    LinearLayout root;

    LinearLayout container;

    //数据
    private List<Schedule> dataSource;

    private List<TextView> textViews;
    private List<LinearLayout> layouts;

    //当前课程
    private int curCourse=1;

    //多少项
    private int courseCount;

    private ICourseDetailMenuView.OnCourseDetailMenuItemClickedListener onCourseDetailMenuItemClickedListener;

    public CourseDetailView(Context context) {
        this(context, null);
    }

    /**
     * 获取Item点击监听
     * @return
     */
    public ICourseDetailMenuView.OnCourseDetailMenuItemClickedListener onCourseDetailMenuItemClickedListener() {
        if(onCourseDetailMenuItemClickedListener==null) onCourseDetailMenuItemClickedListener= (ICourseDetailMenuView.OnCourseDetailMenuItemClickedListener) new OnCourseDetailMenuItemClickedAdapter();
        return onCourseDetailMenuItemClickedListener;
    }

    /**
     * 设置Item点击监听
     * @param onCourseDetailMenuItemClickedListener
     * @return
     */
    public CourseDetailView callback(ICourseDetailMenuView.OnCourseDetailMenuItemClickedListener onCourseDetailMenuItemClickedListener) {
        this.onCourseDetailMenuItemClickedListener = onCourseDetailMenuItemClickedListener;
        return this;
    }

    /**
     * 设置数据源
     * @param list
     * @return
     */
    public CourseDetailView source(List<? extends ScheduleEnable> list) {
        data(ScheduleSupport.transform_only_names(list));
        return this;
    }

    /**
     * 设置数据源
     * @param scheduleList
     * @return
     */
    public CourseDetailView data(List<Schedule> scheduleList) {
        if (scheduleList == null) return null;
        this.dataSource = scheduleList;
        return this;
    }

    /**
     * 获取数据源
     * @return
     */
    public List<Schedule> dataSource() {
        if (dataSource == null) dataSource = new ArrayList<>();
        return dataSource;
    }

    /**
     * 设置当前课程
     * @param curCourse
     * @return
     */
    public CourseDetailView curCourse(int curCourse) {
        if(curCourse<1) curCourse=1;
        this.curCourse = curCourse;
        return this;
    }


    @Override
    public int courseCount() {
        return 0;
    }

    /**
     * 设置项数
     * @param count
     * @return
     */
    public CourseDetailView courseCount(int count) {
        if (count <= 0) return this;
        this.courseCount = count;
        return this;
    }

    public int getcourseCount() {
        return courseCount;
    }

    public CourseDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflate = LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        mInflate.inflate(R.layout.item_coursedetail_menu, this);
        container=findViewById(R.id.id_coursedetailmenu_container);
        root=findViewById(R.id.id_course_layout);
    }

    /**
     * 显示课程菜单选择布局
     */
    public CourseDetailView showView() {
        if(curCourse<1) curCourse(1);
        if(curCourse>getcourseCount()) curCourse=courseCount;

        container.removeAllViews();
        //每个textview
        textViews=new ArrayList<>();
        layouts = new ArrayList<>();

        for (int i = 0; i < courseCount; i++) {
            View view = mInflate.inflate(R.layout.xml_coursedetail_name, null);
            final TextView SingleNameText=view.findViewById(R.id.id_coursedetail_menu_name);
            final LinearLayout perLayout=view.findViewById(R.id.id_coursedetailview_layout);

            //背景颜色
            switch (i%4+1){
                case 1:
                    SingleNameText.setBackgroundResource(R.color.coursedetail_color_1);
                    break;
                case 2:
                    SingleNameText.setBackgroundResource(R.color.coursedetail_color_2);
                    break;
                case 3:
                    SingleNameText.setBackgroundResource(R.color.coursedetail_color_3);
                    break;
                case 4:
                    SingleNameText.setBackgroundResource(R.color.coursedetail_color_4);
                    break;
            }

            SingleNameText.setText(dataSource().get(i).getName());
            SingleNameText.getLayoutParams().height = 300;
            final int finalI = i;
            SingleNameText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCourseDetailMenuItemClickedListener().onCourseDetailMenuClicked(finalI);
                }
            });
            layouts.add(perLayout);
            textViews.add(SingleNameText);
            container.addView(view);
        }

        return this;
    }



}

