package com.example.sjeong.alarmcall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by SJeong on 2017-05-24.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
