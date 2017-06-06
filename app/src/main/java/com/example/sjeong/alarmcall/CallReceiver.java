package com.example.sjeong.alarmcall;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CallReceiver extends BroadcastReceiver {

    private String Tag = "test MyReciver";
    public static TelephonyManager TelMag;
    private static String callstate;
    private Context callcontext;
    private PhoneStateListener phonelistener;
    private HandleRing handleRing;
    private SharedPreferences preferences;
    private int latercallonoff;
    private String Phonename;
    private DBManager dbManager;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (dbManager == null) {
            dbManager = new DBManager(context, "AlarmCall", null, 1);
            dbManager.ReadDB();
        }

        // 나중에 알림 정보
        preferences = context.getSharedPreferences("Later", Activity.MODE_PRIVATE);
        if(preferences.getString("onoff", "off").equals("off"))
            latercallonoff=0;
        else
            latercallonoff=1;

        // 현재모드 받아오기
        preferences = context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);
        // 설정되어 있는 모드가 없는 경우
        if(preferences.getString("set", "off").equals("off")) {
            Log.i(Tag, "mode off");
            return;
        }

        callcontext = context;
        Log.i(Tag, "call receiver start");

        // 브로드 캐스트 같은 상황에 여러 번 수신되는 경우
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state.equals(callstate))
            return;
        else
            callstate = state;

        // 수신차단
        TelMag = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //전화 수신음
        handleRing = new HandleRing(callcontext);

        // 전화 수신 리스너
        phonelistener = new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING: // 전화 수신
                        Log.i(Tag, "call CALL_STATE_RINGING");
                        handleRing.changeRing(5);
                        // 1이면 즐겨찾기 2이면 즐겨찾기 외에 저장된 번호 3이면 모르는 번호
                        int contactNumber = getContacts(incomingNumber);
                        // 부재중전화 횟수
                        int calllogCount = getCallLog(incomingNumber);
                        // 모드에 따라 수신음 결과 분석
                        ReceiveAnalysisResult(contactNumber, calllogCount, incomingNumber);
                        break;
                    case TelephonyManager.CALL_STATE_IDLE: // 전화 종료
                        Log.i(Tag, "call CALL_STATE_IDLE");
                        handleRing.changeRing(4); // 수신음 원상복귀
                        if(latercallonoff==1){PopupServiceOff();}
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:  // 전화 중
                        Log.i(Tag, "call CALL_STATE_OFFHOOK");
                        if(latercallonoff==1){PopupServiceOff();}
                        break;
                    default:
                        break;
                }
                TelMag.listen(phonelistener, PhoneStateListener.LISTEN_NONE);
            }
        };
        TelMag.listen(phonelistener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    // 모드에 따라 출력내용 결정
    private void ReceiveAnalysisResult(int contactNumber, int calllogCount, String incomingNumber) {
        int star, contact, unknown, count;

        star = preferences.getInt("star", 4);
        contact = preferences.getInt("contact", 4);
        unknown = preferences.getInt("unknown", 4);
        count = preferences.getInt("count", 0);

        if(count<=calllogCount+1) {
            handleRing.changeRing(1); // 긴급전화는 벨소리
            if(latercallonoff==1){PopupServiceOn(incomingNumber);}
            return;
        }

        switch(contactNumber){
            case 1:
                if(star==4)
                    EndCall(incomingNumber);
                else {
                    handleRing.changeRing(star);
                    if(latercallonoff==1){PopupServiceOn(incomingNumber);}
                }
                break;
            case 2:
                if(contact==4)
                    EndCall(incomingNumber);
                else{
                    handleRing.changeRing(contact);
                    if(latercallonoff==1){PopupServiceOn(incomingNumber);}
                }
                break;
            case 3:
                if(unknown==4)
                    EndCall(incomingNumber);
                else{
                    handleRing.changeRing(unknown);
                    if(latercallonoff==1){PopupServiceOn(incomingNumber);}
                }
                break;
        }
    }

    // 전화번호부 정보 확인
    private int getContacts(String phoneNumber){

        Log.i(Tag, "call receiver contacts " + phoneNumber);
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        String[] selectionArgs = null;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.STARRED,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        };

        Cursor cursor = callcontext.getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);

        if (cursor.moveToFirst()) {
            do {
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String star = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                number = number.replace(" ", "");
                number = number.replace("-", "");
                if (phoneNumber.equals(number)) {
                    if (star.equals("1")) {
                        Phonename =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        return 1;
                    } // 즐겨찾기
                    else {
                        Log.i(Tag, "know Number " + phoneNumber);
                        Phonename =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        return 2;
                    } // 전화번호부에 있는 번호
                }
            } while (cursor.moveToNext());

            // 없는 번호
            Log.i(Tag, "unknow Number");
            return 3;
        } else
            return 3; // 주소록에 번호가 없는 경우
    }

    // 5분전에온 부재중 전화 횟수
    private int getCallLog(String phoneNumber) {
        int count = 0; // 부재중 횟수
        Calendar calendar;

        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = new String[]{
                CallLog.Calls.TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
        };
        String selection = CallLog.Calls.TYPE +"!=2";

        // 부재중전화 권한 확인 (후에 작성해야 할 부분!!)
        if (ActivityCompat.checkSelfPermission(callcontext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i(Tag, "부재중 전화 권한!!");
            return 0;
        }
        else {
            Cursor cursor = callcontext.getContentResolver().query(uri, projection, selection, null, null);

            calendar = Calendar.getInstance(); // 현재시간
            // 현재 모드 시간 가져오기!!
            int time = preferences.getInt("time", 0);
            calendar.add(Calendar.MINUTE, -time);  // 설정 시간 전
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
            Log.i(Tag, "stringdate " + dateformat.format(calendar.getTime())); // 5분전 시간 출력

            if (cursor.moveToFirst()) {
                do {
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    number = number.replace(" ", "");
                    number = number.replace("-", "");
                    long duration=  cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
                    String type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                    if (phoneNumber.equals(number) && (duration==0 || type.equals("3"))){
                        long longcalldate = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                        Date calldate = new Date(longcalldate);

                        if(calendar.getTime().before(calldate)) { // 5분 전 부재중 전화 수
                            Log.i(Tag, "이전기록만 " + dateformat.format(calldate)+" type : "+type);
                            count++;
                        }
                    }
                } while (cursor.moveToNext());
            }

            Log.i(Tag, "부재중 수 " +count);
            return count;
        }
    }

    //통화종료
    private void EndCall(String incommingnumber){

        SmsManager smsManager = SmsManager.getDefault();

        try {
            Class c = Class.forName(TelMag.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(TelMag);
            telephonyService.endCall();

            SharedPreferences smspreferences = callcontext.getSharedPreferences("Sms", Activity.MODE_PRIVATE);
            if(smspreferences.getString("onoff", "off").equals("on")) {
                smsManager.sendTextMessage(incommingnumber, null, preferences.getString("sms",""), null, null);
            }

            Log.i(Tag,"end call");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PopupServiceOn(String incommingnumber){
        Intent intent = new Intent(callcontext, CallService.class);
        intent.putExtra("number", incommingnumber);
        intent.putExtra("name", Phonename);
        callcontext.startService(intent);

    }

    private void PopupServiceOff(){
        Intent intent = new Intent(callcontext, CallService.class);
        callcontext.stopService(intent);
    }
}



