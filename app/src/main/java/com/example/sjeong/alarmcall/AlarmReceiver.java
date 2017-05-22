package com.example.sjeong.alarmcall;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver{
    private String Tag = "test Receiver1";
    private DBManager dbManager;

    public void onReceive(Context context, Intent intent) {

        // DB생성
        if (dbManager == null) {
            dbManager = new DBManager(context, "AlarmCall", null, 1);
            dbManager.ReadDB();
        }

        Log.i(Tag, "receiver start");
        boolean setRec = intent.getBooleanExtra("alReceiver", Boolean.TRUE); // ※ True : defaultValue
        int id = Integer.parseInt(intent.getStringExtra("id")); // 스케줄 id 불러옴

        if(setRec) { // true일 때 시작 스케줄
            Intent i = new Intent(context,ScheduleSetActivity.class);
            PendingIntent p = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            //바꿀 모드 chgMode
            Schedule schedule = dbManager.getScheduleId(id);
            Mode mode = dbManager.getMode(schedule.getModename());
            SharedPreferences preferences = context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);
            SharedPreferences preferencesschedule = context.getSharedPreferences("Schedule", Activity.MODE_PRIVATE);

            Log.i(Tag, "receiver start"+schedule.getModename());

            // premode 설정
            if(preferencesschedule.getInt("id", -1)==-1) {
                if (preferences.getString("set", "off").equals("off"))
                    schedule.setPremodename("null");
                else
                    schedule.setPremodename(preferences.getString("name", "null"));
            }
            else {
                Schedule preschedule = dbManager.getScheduleId(preferencesschedule.getInt("id", -1));
                schedule.setPremodename(preschedule.getPremodename());
            }
            dbManager.updateSchedule(schedule);

            // 현재모드 변경
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("set", "on");
            editor.putString("name", mode.getName());
            editor.putInt("star", mode.getStar());
            editor.putInt("contact", mode.getContact());
            editor.putInt("unknown", mode.getUnknown());
            editor.putInt("time", mode.getTime());
            editor.putInt("count", mode.getCount());
            editor.commit();

            // 현재 실행중인 스케줄로 등록
            editor = preferencesschedule.edit();
            editor.putInt("id", id);
            editor.commit();

            Log.i(Tag, "changing to " + schedule.getModename());
            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(android.R.drawable.stat_notify_chat).setTicker("HETT").setWhen(System.currentTimeMillis())
                    .setContentTitle("Schedule starts").setContentText("change mode")
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(p).setAutoCancel(true);

            notificationmanager.notify(1, builder.build());
        }
        else if(!setRec){ // false일 때 종료 스케줄

            SharedPreferences preferences = context.getSharedPreferences("Schedule", Activity.MODE_PRIVATE);

            if(preferences.getInt("id", -1)==id){
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("id", -1);
                editor.commit();

                Intent i = new Intent(context, ScheduleSetActivity.class);
                PendingIntent p = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                Schedule schedule = dbManager.getScheduleId(id);

                preferences = context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);
                editor = preferences.edit();

                if(schedule.getPremodename().equals("null")){
                    editor.putString("set", "off");
                    editor.commit();
                }
                else{
                    Mode mode = dbManager.getMode(schedule.getPremodename());

                    editor.putString("set", "on");
                    editor.putString("name", mode.getName());
                    editor.putInt("star", mode.getStar());
                    editor.putInt("contact", mode.getContact());
                    editor.putInt("unknown", mode.getUnknown());
                    editor.putInt("time", mode.getTime());
                    editor.putInt("count", mode.getCount());
                    editor.commit();
                }

                // 위젯 변경
                Intent wintent = new Intent(context, AppWidget.class);
                wintent.setAction("com.example.sjeong.AlarmCall.CHANGE");
                PendingIntent pendindintent = PendingIntent.getBroadcast(context, 0, wintent, 0);
                try {
                    pendindintent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }

                if(schedule.getMon() + schedule.getFri() + schedule.getSat() + schedule.getSun() + schedule.getThu() + schedule.getTue() + schedule.getWed()==0) {
                    schedule.setOnoff(0);
                    dbManager.updateSchedule(schedule);
                    Log.i(Tag, "반복 없음");
                }

                Log.i(Tag, "back to " + schedule.getPremodename());
                NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(context);
                builder.setSmallIcon(android.R.drawable.stat_notify_chat).setTicker("HETT").setWhen(System.currentTimeMillis())
                        .setContentTitle("Schedule ends").setContentText("back to original mode")
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(p).setAutoCancel(true);

                notificationmanager.notify(1, builder.build());
                Log.i(Tag, "End receiver finish");
            }
        }

    }
}
