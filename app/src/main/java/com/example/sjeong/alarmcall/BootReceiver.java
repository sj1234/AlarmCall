package com.example.sjeong.alarmcall;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {
    private String Tag = "test BootReceiver";
    private DBManager dbManager;
    private AlarmManager am;
    public void onReceive(Context context, Intent intent) { // 테스트용
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.i(Tag, "Bootreceiver start");
            Calendar calendar= Calendar.getInstance();
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            intent = new Intent(context, BootTimeService.class);
            PendingIntent sender = PendingIntent.getService(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
// 부팅 후 3분 뒤에 실행되도록.
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+(3*60*1000), sender);
            Log.i(Tag, "Bootreceiver finish");
        }
    }
    /*
    // 실제 동작하는 부분
    public void onReceive(Context context, Intent intent) {
        int finId=1;
        int i =1; //id는 1부터니까.

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            try {
                // DB생성
                if (dbManager == null) {
                    dbManager = new DBManager(context, "AlarmCall", null, 1);
                    dbManager.ReadDB();
                }
                // Schedule schedule = dbManager.getScheduleId();를 써야하나 ?? 여기에 맨 마지막 schedule id 즉, finId를 불러와서.
                for(;i<finId;i++){
                    String[] scheduletag = v.getTag().toString().split("/"); // ??View v를 onReceive에 추가하면 안되고,
                    Schedule schedule = dbManager.getSchedule(Integer.parseInt(scheduletag[0]));
                    dbManager.updateSchedule(schedule);
                    // 각 스케줄의 시작시간,종료시간.
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

                    setAlarm(context, schedule.getId(), Boolean.TRUE,calStart);
                    setAlarm(context, schedule.getId(), Boolean.FALSE,calEnd);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAlarm(Context context,int id, Boolean rec,Calendar calTime){
        am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
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
    */
}
