package com.example.sjeong.alarmcall;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleSetActivity extends AppCompatActivity implements View.OnClickListener{

    private ToggleButton toggleSun,toggleMon,toggleTue,toggleWed,toggleThu,toggleFri,toggleSat;
    private DBManager dbManager;
    private Schedule schedule;
    private String position;
    private TextView starttext;
    private TextView finishtext;

    Calendar calNow1 = Calendar.getInstance();
    Calendar calSet = (Calendar) calNow1.clone();
    Calendar calNow2 = Calendar.getInstance();
    Calendar calReset = (Calendar) calNow2.clone();

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


        starttext = (TextView) findViewById(R.id.amStart);
        starttext.setOnClickListener((View.OnClickListener) this);
        finishtext = (TextView) findViewById(R.id.amFinish);
        finishtext.setOnClickListener((View.OnClickListener) this);
        ImageButton startbtn =  (ImageButton)findViewById(R.id.timeSet);
        startbtn.setOnClickListener((View.OnClickListener) this);
        ImageButton finishbtn =  (ImageButton)findViewById(R.id.timeReset);
        finishbtn.setOnClickListener((View.OnClickListener) this);

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
            if(schedule.getSun()==1)
                toggleSun.setChecked(Boolean.TRUE);
            if(schedule.getMon()==1)
                toggleMon.setChecked(Boolean.TRUE);
            if(schedule.getTue()==1)
                toggleTue.setChecked(Boolean.TRUE);
            if(schedule.getWed()==1)
                toggleWed.setChecked(Boolean.TRUE);
            if(schedule.getThu()==1)
                toggleThu.setChecked(Boolean.TRUE);
            if(schedule.getFri()==1)
                toggleFri.setChecked(Boolean.TRUE);
            if(schedule.getSat()==1)
                toggleSat.setChecked(Boolean.TRUE);
        }
        else // 새로 추가인 경우
        {
            schedule = new Schedule();
            schedule.setMon(0);
            schedule.setTue(0);
            schedule.setWed(0);
            schedule.setThu(0);
            schedule.setFri(0);
            schedule.setSat(0);
            schedule.setSun(0);
            delete.setText("취소");
        }

        toggleSun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){schedule.setSun(1);}
                else{schedule.setSun(0);}
            }
        });
        toggleMon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){schedule.setMon(1);}
                else{schedule.setMon(0);}
            }
        });
        toggleTue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){schedule.setTue(1);}
                else{schedule.setTue(0);}
            }
        });
        toggleWed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){schedule.setWed(1);}
                else{schedule.setWed(0);}
            }
        });
        toggleThu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){schedule.setThu(1);}
                else{schedule.setThu(0);}
            }
        });
        toggleFri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){schedule.setFri(1);}
                else{schedule.setFri(0);}
            }
        });
        toggleSat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){schedule.setSat(1);}
                else{schedule.setSat(0);}
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position!=null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleSetActivity.this);
                    builder.setMessage("스케줄을 삭제하시겠습니까?");
                    builder.setNegativeButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if(schedule.getOnoff()==1) {
                                        // 알람종료
                                        Intent intent = new Intent(ScheduleSetActivity.this,AlarmReceiver.class);
                                        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                        PendingIntent amIntent = PendingIntent.getBroadcast(ScheduleSetActivity.this,schedule.getId()*2,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                        am.cancel(amIntent);
                                        amIntent = PendingIntent.getBroadcast(ScheduleSetActivity.this,(schedule.getId()*2)+1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                        am.cancel(amIntent);
                                    }
                                    dbManager.deleteSchedule(schedule.getId());
                                    Toast.makeText(getApplicationContext(),"스케줄을 삭제하였습니다..",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                    builder.setPositiveButton("아니오", null);
                    builder.show();
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

                    String starttime = schedule.getStart().toString();
                    String endtime = schedule.getEnd().toString();
                    String[] start = starttime.split(":");
                    String[] end = endtime.split(":");

                    if(Integer.parseInt(start[0])<Integer.parseInt(end[0]) || (Integer.parseInt(start[0])==Integer.parseInt(end[0]) && Integer.parseInt(start[1])<Integer.parseInt(end[1]))){

                        if(position!=null) {
                            Toast.makeText(ScheduleSetActivity.this, "수정 저장 ", Toast.LENGTH_SHORT).show();
                            dbManager.updateSchedule(schedule);
                        }
                        else {
                            Toast.makeText(ScheduleSetActivity.this, "저장", Toast.LENGTH_SHORT).show();
                            dbManager.insertSchedule(schedule);
                        }
                        ScheduleSetActivity.this.finish();

                    }
                    else{
                        Toast.makeText(ScheduleSetActivity.this, "스케줄 시작시간이 종료시간 이전이여야 합니다. ", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(ScheduleSetActivity.this, "모든 내용을 입력하십시오", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.amStart:
            case R.id.timeSet :
                Calendar c = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ScheduleSetActivity.this, timeListener1,
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog.setTitle("START TIME");
                timePickerDialog.show();
                break;
            case R.id.amFinish:
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
