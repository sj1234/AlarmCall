package com.example.sjeong.alarmcall;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleSetActivity extends AppCompatActivity {

    private String Tag = "test ScheduleSetActivity";
    private ToggleButton toggleSun,toggleMon,toggleTue,toggleWed,toggleThu,toggleFri,toggleSat;
    private Switch aSwitch;
    private DBManager dbManager;
    private Schedule schedule;
    private String position;
    private TextView starttext;
    private TextView finishtext;

    Calendar calNow1 = Calendar.getInstance();
    Calendar calSet = (Calendar) calNow1.clone();
    Calendar calNow2 = Calendar.getInstance();
    Calendar calReset = (Calendar) calNow2.clone();
    Boolean setRec = Boolean.FALSE; // True?대㈃ set False?대㈃ reset

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_set);

        // 필요한 내용 설정
        Intent intent = getIntent();
        position = intent.getStringExtra("Position");
        if (dbManager == null) {
            dbManager = new DBManager(ScheduleSetActivity.this, "AlarmCall", null, 1);
            dbManager.ReadDB();
        }


        // 화면 설정
        toggleSun = (ToggleButton) findViewById(R.id.toggle_sun);
        toggleMon = (ToggleButton) findViewById(R.id.toggle_mon);
        toggleTue = (ToggleButton) findViewById(R.id.toggle_tue);
        toggleWed = (ToggleButton) findViewById(R.id.toggle_wed);
        toggleThu = (ToggleButton) findViewById(R.id.toggle_thu);
        toggleFri = (ToggleButton) findViewById(R.id.toggle_fri);
        toggleSat = (ToggleButton) findViewById(R.id.toggle_sat);


        aSwitch =(Switch)findViewById(R.id.amSw);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Toast.makeText(ScheduleSetActivity.this, "ON", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScheduleSetActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        starttext = (TextView) findViewById(R.id.amStart);
        finishtext = (TextView) findViewById(R.id.amFinish);

        ArrayList<String> arraylist = dbManager.getModesName();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraylist);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        Button delete = (Button) findViewById(R.id.delete);

        // 수정인 경우
        if(position!=null) {
            schedule = dbManager.getSchedule(Integer.parseInt(position));
            starttext.setText("시작 시간 : "+schedule.getStart());
            finishtext.setText("종료 시간 : "+schedule.getEnd());
            for(int i=0;i<arraylist.size();i++){
                if(arraylist.get(i).toString().equals(schedule.getModename())) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
        else // 새로 추가인 경우
        {
            schedule = new Schedule();
            delete.setText("취소");
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position!=null) {
                    dbManager.deleteSchedule(schedule.getId());
                    finish();
                }
                else
                    finish();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                schedule.setModename(""+parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        SharedPreferences preferences = getSharedPreferences("Mode", Activity.MODE_PRIVATE);
        schedule.setPremodename(preferences.getString("name",""));

        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (schedule.getModename() != null && schedule.getStart() != null && schedule.getEnd() != null) {
                    schedule.setMon(0);
                    schedule.setTue(0);
                    schedule.setWed(0);
                    schedule.setThu(0);
                    schedule.setFri(0);
                    schedule.setSat(0);
                    schedule.setSun(0);

                    if(position!=null) {
                        Toast.makeText(ScheduleSetActivity.this, "저장 "+schedule.getId(), Toast.LENGTH_SHORT).show();
                        dbManager.updateSchedule(schedule);
                    }
                    else {
                        Toast.makeText(ScheduleSetActivity.this, "저장", Toast.LENGTH_SHORT).show();
                        dbManager.insertSchedule(schedule);
                    }

                    ScheduleSetActivity.this.finish();
                }
                else
                    Toast.makeText(ScheduleSetActivity.this, "모든 내용을 입력하십시오", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onButton(final View v) {
        switch (v.getId()) {
            case R.id.timeSet :
                Calendar c = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ScheduleSetActivity.this, timeListener1,
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog.setTitle("START TIME");
                timePickerDialog.show();
                break;
            case R.id.timeReset :
                Calendar c2 = Calendar.getInstance();
                TimePickerDialog timePickerDialog2 = new TimePickerDialog(
                        ScheduleSetActivity.this, timeListener2,
                        c2.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.MINUTE), false);
                timePickerDialog2.setTitle("END TIME");
                timePickerDialog2.show();
                break;
            default:
                break;
        }
    }

    TimePickerDialog.OnTimeSetListener timeListener1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            if (calSet.compareTo(calNow1) <= 0) {
                calSet.add(Calendar.DATE, 1);
            }

            // 수정????   원래 schedule.setStart(calSet.toString());
            SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
            schedule.setStart(dateformat.format(calSet.getTime()));

            starttext.setText("시작 시간 : "+ dateformat.format(calSet.getTime()));
        }
    };

    TimePickerDialog.OnTimeSetListener timeListener2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            calReset.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calReset.set(Calendar.MINUTE, minute);
            calReset.set(Calendar.SECOND, 0);
            calReset.set(Calendar.MILLISECOND, 0);
            if (calReset.compareTo(calNow2) <= 0) {
                calReset.add(Calendar.DATE, 1);
            }

            // 수정????   원래 schedule.setEnd(calReset.toString());
            SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
            schedule.setEnd(dateformat.format(calReset.getTime()));

            finishtext.setText("종료 시간 : "+ dateformat.format(calReset.getTime()));
        }
    };
}
