package com.example.sjeong.alarmcall;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class LaterCall extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Log.i("test LaterAlarm","아까 거부한 전화 알람");
        // 나중에 알림 전화에 대해 알람 울리기!
        Toast.makeText(context, "아까 거부한 전화 알람", Toast.LENGTH_SHORT).show();

        // 전화번호 정보 받아오기
        String number = intent.getStringExtra("phonenumber");
        number = number.replace(" ", "");
        number = number.replace("-", "");
        String name = intent.getStringExtra("name");

        // 푸시 알람
        Uri uri = Uri.parse("tel:"+number);
        NotificationManager notificationmanager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        PendingIntent pendingintent = PendingIntent.getActivity(context, 0, new Intent(Intent.ACTION_CALL, uri), PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder builder = new Notification.Builder(context);

        builder.setSmallIcon(R.drawable.call).setTicker("AlarmCall 나중에 알림").setWhen(System.currentTimeMillis())
                .setContentTitle("AlarmCall 나중에 알림").setContentText("전화번호 : "+number)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingintent).setAutoCancel(true);

        builder.setPriority(Notification.PRIORITY_MAX);
        notificationmanager.notify(0, builder.build());
    }
}
