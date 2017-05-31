package com.example.sjeong.alarmcall;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {
    private String Tag = "test BootReceiver";
    private DBManager dbManager;
    private AlarmManager am;

    // 부팅 시 스케줄 등록
    public void onReceive(Context context, Intent intent) {

        Log.i(Tag, "Bootreceiver start");

        // DB생성
        if (dbManager == null) {
            dbManager = new DBManager(context, "AlarmCall", null, 1);
            dbManager.ReadDB();
        }

        ArrayList<Schedule> arraySchedule = dbManager.getSchedules();

        for(Schedule schedule:arraySchedule){
            if(schedule.getOnoff()==1){
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

                amSet(context, calStart, schedule.getId(), Boolean.TRUE);
                amSet(context, calEnd, schedule.getId(), Boolean.FALSE);
            }
        }
    }

    // 알람설정함수
    public void amSet(Context context, Calendar calTime, int id, Boolean rec){

        am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        if(rec){ // 시작시간
            intent.putExtra("alReceiver", Boolean.TRUE);
            intent.putExtra("id", id+"");
            PendingIntent amIntent = PendingIntent.getBroadcast(context, id*2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.setExact(AlarmManager.RTC_WAKEUP,calTime.getTimeInMillis(), amIntent);
            Log.i("test schedule", " 스케줄 시작 시간 :  " + calTime.getTimeInMillis());
        }
        else{  //종료시간
            intent.putExtra("alReceiver",Boolean.FALSE);
            intent.putExtra("id",id+"");
            PendingIntent amIntent = PendingIntent.getBroadcast(context, (id*2)+1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.setExact(AlarmManager.RTC_WAKEUP, calTime.getTimeInMillis(), amIntent);
            Log.i("test schedule", " 스케줄 종료 시간 :  "+calTime.getTimeInMillis());
        }
    }
}
