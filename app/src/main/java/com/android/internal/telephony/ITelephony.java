package com.android.internal.telephony;

/**
 * Created by SJeong on 2017-04-29.
 */

public interface ITelephony {

    boolean endCall();

    void answerRingingCall();

    void silenceRinger();
}