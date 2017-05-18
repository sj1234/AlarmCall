package com.example.sjeong.alarmcall;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by SJeong on 2017-05-01.
 */

public class HandleRing {
    private Context mContext;
    private AudioManager myAudioManager;
    private static int ringerMode;
    private String Tag = "test HandleRing";

    public HandleRing(Context context) {
        this.mContext = context;
        myAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public void getRing() {
        ringerMode= myAudioManager.getRingerMode();
        Log.i(Tag, "get ringmode "+ringerMode);
    }

    public void setRing(){
        // 기존 설정으로 돌아가기.
        Log.i(Tag, "comeback "+ringerMode);
        myAudioManager.setRingerMode(ringerMode);
    }

    public void ring() { //1:RINGER_MODE_VIBRATE 2:RINGER_MODE_NORMAL 0:RINGER_MODE_SILENT
        // 벨소리로 변경;
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
    public void vibrate() {
        // 진동으로 변경;
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }
    public void silent() {
        // 무음으로 변경;
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    public void changeRing(int i) {
        // int i 가 1이면 벨소리, 2이면 진동, 3이면 무음, 4이면 이전상태로
        Log.i("test handleRing", "change ring");
        switch (i) {
            case 1:
                getRing();
                ring();  Log.i(Tag, "ring");
                break;
            case 2:
                getRing();
                vibrate();
                Log.i(Tag, "vibrate");
                break;
            case 3:
                getRing();
                silent();  Log.i(Tag, "silent");
                break;
            case 4:
                setRing();
                break;
            default:
                Log.i(Tag, "no Ringtone");
                break;
        }
    }
}
