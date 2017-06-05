package com.example.sjeong.alarmcall;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                Log.i("test 권한","권한 X");
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_CALL_LOG, Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS,
                            Manifest.permission.RECEIVE_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
                else{
                    Log.i("test 권한","권한 다시보지 않기");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("권한 허용");
                    alertDialogBuilder
                            .setMessage("필요한 권한\n\n1. 전화번호부의 정보에 따라 수신음을 다르게 하기 때문에 전화번호부 권한이 필요합니다.\n\n" +
                                    "2.나중에 알림 기능 사용을 위해 전화권한이 필요합니다.\n\n" +
                                    "3.차단번호 문자 기능 사용을 위해 SMS권한이 필요합니다.\n\n"+"4.모드의 이미지를 사용자설정 이미지로 가능하게 하기위해 권한이 필요합니다.\n\n"+
                                    "설정 >  권한 에서 모든 권한을 설정해주신 후 접속해 주십시오.")
                            .setCancelable(false)
                            .setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_CONTACTS,
                                                    Manifest.permission.READ_CALL_LOG, Manifest.permission.CALL_PHONE,
                                                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS,
                                                    Manifest.permission.RECEIVE_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            } else
                StartMainActivity();
        }
        else
            StartMainActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permission[], int[] grantResults){
        if(requestCode==0) {
            if (grantResults[0] + grantResults[1] + grantResults[2] + grantResults[3]+ grantResults[4]+ grantResults[5]+ grantResults[6]>=0) { // 권한 다 허용시
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
