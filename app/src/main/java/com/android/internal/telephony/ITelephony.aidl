package com.android.internal.telephony;

/**
 * Created by SJeong on 2017-04-28.
 */

interface ITelephony {
    boolean endCall();

    void answerRingingCall();

    void silenceRinger();
}
