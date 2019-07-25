package com.example.library_activity_timetable.listener;

public interface ICourseDetailMenuView {

        /**
         * WeekView的Item点击监听器
         */
        interface OnCourseDetailMenuItemClickedListener{
            /**
             * 当Item被点击时回调
             * @param week 选择的周次
             */
            void onCourseDetailMenuClicked(int week);
        }
}
