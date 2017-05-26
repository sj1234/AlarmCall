package com.example.sjeong.alarmcall;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    final static String ACTION_CLICK = "com.example.sjeong.AlarmCall.CLICK";
    final static String ACTION_CHANGE = "com.example.sjeong.AlarmCall.CHANGE";
    private int ImageChange;

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
            }
            else{
                views.setImageViewResource(R.id.buttonWidget, preferences.getInt("draw", R.drawable.icon_off));
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
        // 위젯 업데이트
        if(ACTION_CLICK.equals(intent.getAction())){
            SharedPreferences preferences= context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            if(preferences.getString("set", "off").equals("off") && !preferences.getString("name", "null").equals("null")) {
                Toast.makeText(context,"Mode On "+preferences.getString("name", "null"), Toast.LENGTH_LONG).show();
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
                Toast.makeText(context,"Mode Off", Toast.LENGTH_LONG).show();
                editor.putString("set", "off");
                editor.commit();

                // 스케줄 실행중인 경우 ( 스케줄 종료 )
                SharedPreferences preferencesschedule = context.getSharedPreferences("Schedule", Activity.MODE_PRIVATE);
                if(preferencesschedule.getInt("id", -1) > -1) {
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

