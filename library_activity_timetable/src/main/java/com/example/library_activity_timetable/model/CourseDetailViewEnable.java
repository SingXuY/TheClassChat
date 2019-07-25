package com.example.library_activity_timetable.model;

import java.util.List;

public interface CourseDetailViewEnable<T> {
    /**
     * 设置当前周
     * @param curCourse
     * @return
     */
    T curCourse(int curCourse);

    /**
     * 设置项数
     * @param count
     * @return
     */
    T courseCount(int count);

    /**
     * 获取项数
     * @return
     */
    int courseCount();

    /**
     * 设置数据源
     * @param list
     * @return
     */
    T source(List<? extends ScheduleEnable> list);

    /**
     * 设置数据源
     * @param scheduleList
     * @return
     */
    public T data(List<Schedule> scheduleList);

    /**
     * 获取数据源
     * @return
     */
    List<Schedule> dataSource();

    /**
     * 初次构建时调用，显示周次选择布局
     */
    T showView();

}
