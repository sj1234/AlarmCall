package com.example.sjeong.alarmcall;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private DBManager dbManager;
    private ListView listView;
    private AlarmManager am;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // DB생성
        if (dbManager == null) {
            dbManager = new DBManager(getActivity(), "AlarmCall", null, 1);
            dbManager.ReadDB();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule,null);

        listView = (ListView)view.findViewById(R.id.schedulelistView);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_schedule);
        // 이벤트 적용
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScheduleSetActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        ArrayList<Schedule> arrayList = dbManager.getSchedules();
        ScheduleListAdapter listAdapter = new ScheduleListAdapter(getActivity(), R.layout.schedulelistview, arrayList, onClickListener);
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] scheduletag = v.getTag().toString().split("/");
            Intent intent;

            switch (v.getId()) {
                case R.id.scheduletime:
                case R.id.modename:
                case R.id.repeat:
                    if(scheduletag[1].equals("0")) { // 스케줄이 on 인경우 수정 불가, off인 경우만 수정 가능
                        intent = new Intent(getActivity(), ScheduleSetActivity.class);
                        intent.putExtra("Position", scheduletag[0]);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(getActivity(), "스케줄 OFF시에만 수정 가능합니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.scheduleon:
                    Schedule schedule = dbManager.getSchedule(Integer.parseInt(scheduletag[0]));
                    if(scheduletag[1].equals("0")){ // 스케줄이 off 였다가 on된 경우
                        schedule.setOnoff(1);
                        dbManager.updateSchedule(schedule);

                        String starttime = schedule.getStart().toString();
                        String endtime = schedule.getEnd().toString();
                        String[] start = starttime.split(":");
                        String[] end = endtime.split(":");

                        Calendar calStart = Calendar.getInstance();
                        Calendar calEnd = Calendar.getInstance();

                        calStart.set(calStart.get(Calendar.YEAR), calStart.get(Calendar.MONTH) , calStart.get(Calendar.DATE), Integer.parseInt(start[0]), Integer.parseInt(start[1]),0);
                        calEnd.set(calEnd.get(Calendar.YEAR), calEnd.get(Calendar.MONTH) , calEnd.get(Calendar.DATE), Integer.parseInt(end[0]), Integer.parseInt(end[1]),0);

                        if (calStart.compareTo(Calendar.getInstance()) <= 0) {
                            calStart.add(Calendar.DATE, 1);
                            calEnd.add(Calendar.DATE, 1);
                        }

                        amSet(calStart, schedule.getId(), Boolean.TRUE);
                        amSet(calEnd, schedule.getId(), Boolean.FALSE);
                    }
                    else{ // 스케줄이 on이였다가 off된 경우
                        schedule.setOnoff(0);
                        dbManager.updateSchedule(schedule);
                        amClear(schedule.getId());
                    }
                    onResume();
                    break;
                default:
                    break;

            }
        }
    };

    // 알람설정함수
    public void amSet(Calendar calTime, int id, Boolean rec){

        am = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);

        if(rec){ // 시작시간
            intent.putExtra("alReceiver", Boolean.TRUE);
            intent.putExtra("id", id+"");
            PendingIntent amIntent = PendingIntent.getBroadcast(getActivity(), id*2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.setExact(AlarmManager.RTC_WAKEUP,calTime.getTimeInMillis(), amIntent);
            Log.i("test schedule", " 스케줄 시작 시간 :  " + calTime.getTimeInMillis());
        }
        else{  //종료시간
            intent.putExtra("alReceiver",Boolean.FALSE);
            intent.putExtra("id",id+"");
            PendingIntent amIntent = PendingIntent.getBroadcast(getActivity(), (id*2)+1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.setExact(AlarmManager.RTC_WAKEUP, calTime.getTimeInMillis(), amIntent);
            Log.i("test schedule", " 스케줄 종료 시간 :  "+calTime.getTimeInMillis());
        }
    }

    // 알람종료함수
    public void amClear(int id){
        Intent intent = new Intent(getActivity(),AlarmReceiver.class);
        am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        PendingIntent amIntent = PendingIntent.getBroadcast(getActivity(), id*2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent amIntent2 = PendingIntent.getBroadcast(getActivity(), (id*2)+1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(amIntent);
        am.cancel(amIntent2);

        SharedPreferences preferences= getActivity().getSharedPreferences("Schedule", Activity.MODE_PRIVATE);
        if(preferences.getInt("id", -1)==id){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("id", -1);
            editor.commit();
        }

        Log.i("test schedule", "Clear Alarm");
    }
}
