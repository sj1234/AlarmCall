package com.example.sjeong.alarmcall;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by SJeong on 2017-05-01.
 */

public class HandleRing {
    private Context mContext;
    private AudioManager myAudioManager;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String Tag = "test HandleRing";

    public HandleRing(Context context) {
        this.mContext = context;
        myAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        preferences = context.getSharedPreferences("Call", Activity.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void changeRing(int i) {
        // int i 가 1이면 벨소리, 2이면 진동, 3이면 무음, 4이면 이전상태로
        Log.i("test handleRing", "change ring");
        switch (i) {
            case 1:
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                Log.i(Tag, "ring");
                break;
            case 2:
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                Log.i(Tag, "vibrate");
                break;
            case 3:
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                Log.i(Tag, "silent");
                break;
            case 4: // 기존 설정으로 돌아가기.
                myAudioManager.setRingerMode(preferences.getInt("ring", AudioManager.RINGER_MODE_NORMAL));
                break;
            case 5:
                editor.putInt("ring", myAudioManager.getRingerMode());
                editor.commit();
                break;
            default:
                Log.i(Tag, "no Ringtone");
                break;
        }
    }
}
