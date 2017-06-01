package com.example.sjeong.alarmcall;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by kjh on 2017-06-01.
 */

public class Messenger {
    private Context mContext;

    public Messenger(Context mContext) {
        this.mContext = mContext;
    }

    public void sendMessageTo(String phoneNum, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNum, null, message, null, null);

        Toast.makeText(mContext, "Message transmission is completed.",
                Toast.LENGTH_SHORT).show();
    }

}
