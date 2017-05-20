package com.example.sjeong.alarmcall;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.util.Calendar;

public class CallService extends Service {

    protected View view;
    private TextView textcallnumber;
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private Button later;
    private String number;

    @Override
    public void onCreate() {
        super.onCreate();

        // popup 설정
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.9); //Display 사이즈의 90%

        // width, hight, 락스크린상태에도 맨 위로 띄우고 클릭 받음, 뷰를 제외한 부분의 터치 가능(포커스를 가지지 않음),Lock 화면 위로 실행, keygurd 해지, 스크린 킨 상태로 유지
        params = new WindowManager.LayoutParams(
                width,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                PixelFormat.TRANSLUCENT);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        number = intent.getStringExtra("number");

        if( number.equals("off")){
            removePopup();
        }
        else {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE); // 부분레이아웃
            view = layoutInflater.inflate(R.layout.popup, null);

            textcallnumber = (TextView) view.findViewById(R.id.call_number);

            ImageButton close = (ImageButton) view.findViewById(R.id.popup_close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removePopup();
                }
            });

            later = (Button) view.findViewById(R.id.popup_later);
            windowManager.addView(view, params);
            textcallnumber.setText(number);

            later.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LaterCallAlarm(number);
                    Log.i("test later","service to later");
                    EndCall();
                }
            });
        }
        return START_REDELIVER_INTENT;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void removePopup() {
        if (view != null && windowManager != null) windowManager.removeView(view);
        view = null;
    }

    // 나중에 알람이 오도록
    private void LaterCallAlarm(String number){
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        Intent alarmintent= new Intent(this, LaterCall.class);
        alarmintent.putExtra("phonenumber", number); // 전화번호 정보 전달
        PendingIntent pendingintent=PendingIntent.getBroadcast(this, 0, alarmintent, PendingIntent.FLAG_ONE_SHOT);

        Calendar calendar = Calendar.getInstance(); // 현재시간
        calendar.add(Calendar.SECOND, 15);  // 현재시간 10분 후 (test는 15초 후로)
        //RTC_WAKEUP : 지금 시간을 기준으로 알람이 동작, sleep모드여도 실행한다.
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingintent);
    }

    // 통화 차단
    private void EndCall(){
        try {
            Class c = Class.forName(CallReceiver.TelMag.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(CallReceiver.TelMag);
            telephonyService.endCall();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}