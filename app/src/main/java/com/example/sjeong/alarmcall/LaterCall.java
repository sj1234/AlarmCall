package com.example.sjeong.alarmcall;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class LaterCall extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Log.i("test LaterAlarm","아까 거부한 전화 알람");

        // 전화번호 정보 받아오기
        String number = intent.getStringExtra("phonenumber");
        number = number.replace(" ", "");
        number = number.replace("-", "");
        String name = intent.getStringExtra("name");

        String info;
        if(name==null)
            info = "전화번호 : "+number;
        else
            info = name+ " : "+number;

        // 푸시 알람
        Uri uri = Uri.parse("tel:"+number);
        NotificationManager notificationmanager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        PendingIntent pendingintent = PendingIntent.getActivity(context, 0, new Intent(Intent.ACTION_CALL, uri), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);

        builder.setSmallIcon(R.drawable.call).setTicker("AlarmCall 나중에 알림").setWhen(System.currentTimeMillis())
                .setContentTitle("AlarmCall 나중에 알림").setContentText(info)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingintent).setAutoCancel(true);

        builder.setPriority(Notification.PRIORITY_MAX);
        notificationmanager.notify(Integer.parseInt(number), builder.build());
    }
}
