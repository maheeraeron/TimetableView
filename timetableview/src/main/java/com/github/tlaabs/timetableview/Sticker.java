package com.github.tlaabs.timetableview;

import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class Sticker implements Serializable {
    private ArrayList<TextView> view;
    private ArrayList<TimeTableCourse> timeTableCourses;

    public Sticker() {
        this.view = new ArrayList<TextView>();
        this.timeTableCourses = new ArrayList<TimeTableCourse>();
    }

    public void addTextView(TextView v){
        view.add(v);
    }

    public void addSchedule(TimeTableCourse timeTableCourse){
        timeTableCourses.add(timeTableCourse);
    }

    public ArrayList<TextView> getView() {
        return view;
    }

    public ArrayList<TimeTableCourse> getTimeTableCourses() {
        return timeTableCourses;
    }
}
