package com.example.sjeong.alarmcall;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

public class BootTimeService extends Service {
    private String Tag = "test BootTimeService";
    private DBManager dbManager;
    private AlarmManager am;
    public BootTimeService() {
        Log.i(Tag, "BootTimeService start");
    }
    Context context;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //알람리시버 갖다쓴 부분
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (dbManager == null) {
            dbManager = new DBManager(context, "AlarmCall", null, 1);
            dbManager.ReadDB();
        }

        Log.i(Tag, "Service start");
        boolean setRec = intent.getBooleanExtra("alReceiver", Boolean.TRUE); // ※ True : defaultValue
        int id = Integer.parseInt(intent.getStringExtra("id")); // 스케줄 id 불러옴
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Schedule schedule = dbManager.getScheduleId(id);

        if (setRec) { // true일 때 시작 스케줄

            // 반복이 있는 경우
            if (schedule.getMon() + schedule.getFri() + schedule.getSat() + schedule.getSun() + schedule.getThu() + schedule.getTue() + schedule.getWed() > 0) {
                Calendar calStart = Calendar.getInstance();
                calStart.set(calStart.get(Calendar.YEAR), calStart.get(Calendar.MONTH), calStart.get(Calendar.DATE), calStart.get(Calendar.HOUR_OF_DAY), calStart.get(Calendar.MINUTE), 0);

                am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pend = PendingIntent.getBroadcast(context, id * 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                am.cancel(pend);
                am.setExact(AlarmManager.RTC_WAKEUP, calStart.getTimeInMillis() + 24 * 60 * 60 * 1000, pend); // 24시간 후에 다시 set한다.

                if (CompareWeek(schedule) == 0) { // 오늘 반복 아님!
                    Log.i(Tag, "오늘 반복이 아님");
                    return START_REDELIVER_INTENT;
                } else
                    Log.i(Tag, "오늘 반복!");
            }

            PendingIntent p = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            //바꿀 모드 chgMode
            Mode mode = dbManager.getMode(schedule.getModename());
            SharedPreferences preferences = context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);
            SharedPreferences preferencesschedule = context.getSharedPreferences("Schedule", Activity.MODE_PRIVATE);

            Log.i(Tag, "receiver start" + schedule.getModename());

            // premode 설정
            if (preferencesschedule.getInt("id", -1) == -1) {
                if (preferences.getString("set", "off").equals("off"))
                    schedule.setPremodename("null");
                else
                    schedule.setPremodename(preferences.getString("name", "null"));
            } else {
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
            editor.putInt("draw", mode.getDraw());
            editor.commit();

            // 이전 스케줄이 반복이 없을 경우 리스트 색 변화하도록
            if (preferencesschedule.getInt("id", -1) > -1) {
                Schedule preschedule = dbManager.getScheduleId(preferencesschedule.getInt("id", -1));
                if (preschedule.getMon() + preschedule.getFri() + preschedule.getSat() + preschedule.getSun() + preschedule.getThu() + preschedule.getTue() + preschedule.getWed() == 0) {
                    preschedule.setOnoff(0);
                    dbManager.updateSchedule(preschedule);
                    Log.i(Tag, "반복 없음");
                }
            }

            // 현재 실행중인 스케줄로 등록
            editor = preferencesschedule.edit();
            editor.putInt("id", id);
            editor.commit();

            // 위젯 변경
            Intent wintent = new Intent(context, AppWidget.class);
            wintent.setAction("com.example.sjeong.AlarmCall.CHANGE");
            PendingIntent pendindintent = PendingIntent.getBroadcast(context, 0, wintent, 0);
            try {
                pendindintent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

            Log.i(Tag, "changing to " + schedule.getModename());
            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(android.R.drawable.stat_notify_chat).setTicker("HETT").setWhen(System.currentTimeMillis())
                    .setContentTitle("Schedule starts").setContentText("change mode")
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(p).setAutoCancel(true);

            notificationmanager.notify(1, builder.build());
        } else if (!setRec) { // false일 때 종료 스케줄

            // 반복이 있는 경우
            if (schedule.getMon() + schedule.getFri() + schedule.getSat() + schedule.getSun() + schedule.getThu() + schedule.getTue() + schedule.getWed() > 0) {
                Calendar calStart = Calendar.getInstance();
                calStart.set(calStart.get(Calendar.YEAR), calStart.get(Calendar.MONTH), calStart.get(Calendar.DATE), calStart.get(Calendar.HOUR_OF_DAY), calStart.get(Calendar.MINUTE), 0);

                am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pend = PendingIntent.getBroadcast(context, (id * 2) + 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                am.cancel(pend);
                am.setExact(AlarmManager.RTC_WAKEUP, calStart.getTimeInMillis() + 24 * 60 * 60 * 1000, pend); // 24시간 후에 다시 set한다.

                if (CompareWeek(schedule) == 0) { // 오늘 반복 아님!
                    Log.i(Tag, "오늘 반복이 아님");
                    return START_REDELIVER_INTENT;
                } else
                    Log.i(Tag, "오늘 반복!");
            }


            SharedPreferences preferences = context.getSharedPreferences("Schedule", Activity.MODE_PRIVATE);

            if (preferences.getInt("id", -1) == id) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("id", -1);
                editor.commit();
                PendingIntent p = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                preferences = context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);
                editor = preferences.edit();

                if (schedule.getPremodename().equals("null")) {
                    editor.putString("set", "off");
                    editor.commit();
                } else {
                    Mode mode = dbManager.getMode(schedule.getPremodename());

                    if (mode.getName() == null) // premode 가 삭제된 경우 off로
                        editor.putString("set", "off");
                    else {
                        editor.putString("set", "on");
                        editor.putString("name", mode.getName());
                        editor.putInt("star", mode.getStar());
                        editor.putInt("contact", mode.getContact());
                        editor.putInt("unknown", mode.getUnknown());
                        editor.putInt("time", mode.getTime());
                        editor.putInt("count", mode.getCount());
                        editor.putInt("draw", mode.getDraw());
                    }
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

                if (schedule.getMon() + schedule.getFri() + schedule.getSat() + schedule.getSun() + schedule.getThu() + schedule.getTue() + schedule.getWed() == 0) {
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
        return START_REDELIVER_INTENT;
    }

    public int CompareWeek(Schedule schedule) {
        Calendar cal = Calendar.getInstance();
        int week = cal.get(Calendar.DAY_OF_WEEK); // 1 일요일 2 월요일.........

        switch (week) {
            case 1:
                return schedule.getSun();
            case 2:
                return schedule.getMon();
            case 3:
                return schedule.getTue();
            case 4:
                return schedule.getWed();
            case 5:
                return schedule.getThu();
            case 6:
                return schedule.getFri();
            case 7:
                return schedule.getSat();
            default:
                return 0;
        }

    }
}
