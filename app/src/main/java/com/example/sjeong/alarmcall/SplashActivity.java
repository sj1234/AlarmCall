package com.example.sjeong.alarmcall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by SJeong on 2017-05-24.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 권한 설정
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 마시멜로우 이상에서만 확인
            if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission_group.PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission_group.CONTACTS,
                                Manifest.permission_group.PHONE}, 0);
                StartMainActivity();
            } else
                StartMainActivity();
        }
        else
            StartMainActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permission[], int[] grantResults){
        if(requestCode==0) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) { // 권한 다 허용시
                    StartMainActivity();
                }
                else { // 하나라도 권한을 거부할 경우 어플 실행 불가
                    Toast.makeText(SplashActivity.this, "권한을 허용해야 어플이용이 가능합니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    public void StartMainActivity(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
