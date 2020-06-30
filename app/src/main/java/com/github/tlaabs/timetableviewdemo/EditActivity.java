package com.github.tlaabs.timetableviewdemo;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.tlaabs.timetableview.TimeTableCourse;
import com.github.tlaabs.timetableview.Time;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RESULT_OK_ADD = 1;
    public static final int RESULT_OK_EDIT = 2;
    public static final int RESULT_OK_DELETE = 3;

    private Context context;

    private Button deleteBtn;
    private Button submitBtn;
    private EditText subjectEdit;
    private EditText classroomEdit;
    private EditText professorEdit;
    private Spinner daySpinner;
    private TextView startTv;
    private TextView endTv;

    //request mode
    private int mode;

    private TimeTableCourse timeTableCourse;
    private int editIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
    }

    private void init(){
        this.context = this;
        deleteBtn = findViewById(R.id.delete_btn);
        submitBtn = findViewById(R.id.submit_btn);
        subjectEdit = findViewById(R.id.subject_edit);
        classroomEdit = findViewById(R.id.classroom_edit);
        professorEdit = findViewById(R.id.professor_edit);
        daySpinner = findViewById(R.id.day_spinner);
        startTv = findViewById(R.id.start_time);
        endTv = findViewById(R.id.end_time);

        //set the default time
        timeTableCourse = new TimeTableCourse();
        timeTableCourse.setStartTime(new Time(10,0));
        timeTableCourse.setEndTime(new Time(13,30));

        checkMode();
        initView();
    }

    /** check whether the mode is ADD or EDIT */
    private void checkMode(){
        Intent i = getIntent();
        mode = i.getIntExtra("mode",MainActivity.REQUEST_ADD);

        if(mode == MainActivity.REQUEST_EDIT){
            loadScheduleData();
            deleteBtn.setVisibility(View.VISIBLE);
        }
    }
    private void initView(){
        submitBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeTableCourse.setDay(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        startTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context,listener, timeTableCourse.getStartTime().getHour(), timeTableCourse.getStartTime().getMinute(), false);
                dialog.show();
            }

            private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    startTv.setText(hourOfDay + ":" + minute);
                    timeTableCourse.getStartTime().setHour(hourOfDay);
                    timeTableCourse.getStartTime().setMinute(minute);
                }
            };
        });
        endTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context,listener, timeTableCourse.getEndTime().getHour(), timeTableCourse.getEndTime().getMinute(), false);
                dialog.show();
            }

            private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    endTv.setText(hourOfDay + ":" + minute);
                    timeTableCourse.getEndTime().setHour(hourOfDay);
                    timeTableCourse.getEndTime().setMinute(minute);
                }
            };
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_btn:
                if(mode == MainActivity.REQUEST_ADD){
                    inputDataProcessing();
                    Intent i = new Intent();
                    ArrayList<TimeTableCourse> timeTableCourses = new ArrayList<TimeTableCourse>();
                    //you can add more schedules to ArrayList
                    timeTableCourses.add(timeTableCourse);
                    i.putExtra("schedules", timeTableCourses);
                    setResult(RESULT_OK_ADD,i);
                    finish();
                }
                else if(mode == MainActivity.REQUEST_EDIT){
                    inputDataProcessing();
                    Intent i = new Intent();
                    ArrayList<TimeTableCourse> timeTableCourses = new ArrayList<TimeTableCourse>();
                    timeTableCourses.add(timeTableCourse);
                    i.putExtra("idx",editIdx);
                    i.putExtra("schedules", timeTableCourses);
                    setResult(RESULT_OK_EDIT,i);
                    finish();
                }
                break;
            case R.id.delete_btn:
                Intent i = new Intent();
                i.putExtra("idx",editIdx);
                setResult(RESULT_OK_DELETE, i);
                finish();
                break;
        }
    }

    private void loadScheduleData(){
        Intent i = getIntent();
        editIdx = i.getIntExtra("idx",-1);
        ArrayList<TimeTableCourse> timeTableCourses = (ArrayList<TimeTableCourse>)i.getSerializableExtra("schedules");
        timeTableCourse = timeTableCourses.get(0);
        subjectEdit.setText(timeTableCourse.getName());
        classroomEdit.setText(timeTableCourse.getLocation());
        professorEdit.setText(timeTableCourse.getInstructor());
        daySpinner.setSelection(timeTableCourse.getDay());
    }

    private void inputDataProcessing(){
        timeTableCourse.setName(subjectEdit.getText().toString());
        timeTableCourse.setLocation(classroomEdit.getText().toString());
        timeTableCourse.setInstructor(professorEdit.getText().toString());
    }
}
