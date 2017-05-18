package com.example.sjeong.alarmcall;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class CallService extends Service {

    protected View view;
    private TextView textcallnumber;
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private SharedPreferences preferences;

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


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE); // 부분레이아웃
        view = layoutInflater.inflate(R.layout.popup, null);
        textcallnumber = (TextView) view.findViewById(R.id.call_number);
        ImageButton btn_close = (ImageButton) view.findViewById(R.id.popup_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view != null && windowManager != null) windowManager.removeView(view);
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String number = intent.getStringExtra("number");
        if( number.equals("off")){
            removePopup();
        }
        else {
            windowManager.addView(view, params);
            textcallnumber.setText(number);
        }
        return START_REDELIVER_INTENT;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        removePopup();
    }

    public void removePopup() {
        if (view != null && windowManager != null) windowManager.removeView(view);
    }
}
