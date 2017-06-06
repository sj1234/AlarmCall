package com.example.sjeong.alarmcall;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.File;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    final static String ACTION_CLICK = "com.example.sjeong.AlarmCall.CLICK";
    final static String ACTION_CHANGE = "com.example.sjeong.AlarmCall.CHANGE";
    private DBManager dbManager;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

            // 버튼
            SharedPreferences preferences= context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            Intent intent = new Intent(context, AppWidget.class);
            intent.setAction(ACTION_CLICK);
            PendingIntent pendindintent = PendingIntent.getBroadcast(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.buttonWidget, pendindintent);

            if(preferences.getString("set","off").equals("off")) {
                views.setImageViewResource(R.id.buttonWidget, R.drawable.icon_off);
                views.setTextViewText(R.id.widgetname, "Mode OFF");
            }
            else{

                if(preferences.getInt("draw",R.drawable.icon_empty)==0)
                {
                    File imgFile = new  File( preferences.getString("picture", ""));
                    if(imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        RoundedAvatarDrawable tmpRoundedAvatarDrawable = new RoundedAvatarDrawable(myBitmap);
                        views.setImageViewBitmap(R.id.buttonWidget, myBitmap);
                    }
                    else
                        views.setImageViewResource(R.id.buttonWidget, R.drawable.icon_empty);
                }
                else
                    views.setImageViewResource(R.id.buttonWidget, preferences.getInt("draw", R.drawable.icon_off));

                views.setTextViewText(R.id.widgetname, preferences.getString("name", "null") );
            }
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // DB생성
        if (dbManager == null) {
            dbManager = new DBManager(context, "AlarmCall", null, 1);
            dbManager.ReadDB();
        }

        // 위젯 업데이트
        if(ACTION_CLICK.equals(intent.getAction())){
            SharedPreferences preferences= context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            if(preferences.getString("set", "off").equals("off") && !preferences.getString("name", "null").equals("null")) {
                editor.putString("set", "on");
                editor.commit();

                // 스케줄 실행중인 경우 ( 스케줄 종료 )
                SharedPreferences preferencesschedule = context.getSharedPreferences("Schedule", Activity.MODE_PRIVATE);
                if(preferencesschedule.getInt("id", -1) > -1) {
                    SharedPreferences.Editor editorschedule =  preferencesschedule.edit();
                    editorschedule.putInt("id", -1);
                    editorschedule.commit();
                }
            }
            else if(preferences.getString("set", "off").equals("on")){
                editor.putString("set", "off");
                editor.commit();

                // 나중에 알림 해제
                SharedPreferences laterpreferences= context.getSharedPreferences("Later", Activity.MODE_PRIVATE);
                if(laterpreferences.getString("onoff", "off").equals("on")){
                    SharedPreferences.Editor latereditor = laterpreferences.edit();
                    latereditor.putString("onoff", "off");
                    latereditor.commit();
                }

                // 문자 해제
                laterpreferences= context.getSharedPreferences("Sms", Activity.MODE_PRIVATE);
                if(laterpreferences.getString("onoff", "off").equals("on")){
                    SharedPreferences.Editor latereditor = laterpreferences.edit();
                    latereditor.putString("onoff", "off");
                    latereditor.commit();
                }

                // 스케줄 실행중인 경우 ( 스케줄 종료 ) , 이전 스케줄이 반복이 없을 경우 리스트 색 변화
                SharedPreferences preferencesschedule = context.getSharedPreferences("Schedule", Activity.MODE_PRIVATE);
                if(preferencesschedule.getInt("id", -1) > -1) {
                    Schedule preschedule = dbManager.getScheduleId(preferencesschedule.getInt("id", -1));
                    if (preschedule.getMon() + preschedule.getFri() + preschedule.getSat() + preschedule.getSun() + preschedule.getThu() + preschedule.getTue() + preschedule.getWed() == 0) {
                        preschedule.setOnoff(0);
                        dbManager.updateSchedule(preschedule);
                        Log.i("test node schedule 색", "반복 없음");
                    }
                    SharedPreferences.Editor editorschedule =  preferencesschedule.edit();
                    editorschedule.putInt("id", -1);
                    editorschedule.commit();
                }
            }
            else
                Toast.makeText(context,"No Mode Set Before", Toast.LENGTH_LONG).show();


            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            this.onUpdate(context, manager, manager.getAppWidgetIds(new ComponentName(context, AppWidget.class)));
            return;
        }
        else if(ACTION_CHANGE.equals(intent.getAction())) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            this.onUpdate(context, manager, manager.getAppWidgetIds(new ComponentName(context, AppWidget.class)));
            return;
        }
		else
                super.onReceive(context, intent);

    }

    /*
    onUpdate()는 위젯 갱신 주기에 따라 위젯을 갱신할때 호출됩니다
    onEnabled()는 위젯이 처음 생성될때 호출되며, 동일한 위젯의 경우 처음 호출됩니다
    onDisabled()는 위젯의 마지막 인스턴스가 제거될때 호출됩니다
    onDeleted()는 위젯이 사용자에 의해 제거될때 호출됩니다
    */
}

