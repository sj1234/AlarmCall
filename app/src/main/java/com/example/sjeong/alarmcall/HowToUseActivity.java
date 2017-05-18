package com.example.sjeong.alarmcall;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by kjh on 2017-05-18.
 */

public class HowToUseActivity extends AppCompatActivity implements View.OnClickListener {
    final Context context = this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);

        ImageButton mode = (ImageButton) this.findViewById(R.id.mode);
        mode.setOnClickListener((View.OnClickListener) this);

        ImageButton schedule = (ImageButton) this.findViewById(R.id.schedule);
        schedule.setOnClickListener((View.OnClickListener) this);

        ImageButton etc = (ImageButton) this.findViewById(R.id.etc);
        etc.setOnClickListener((View.OnClickListener) this);


    }


    @Override
    public void onClick(View v) {
        Intent intent=null;

        switch (v.getId()) {
            case R.id.mode:
                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
                        context);

                // 제목셋팅
                alertDialogBuilder1.setTitle("모드 사용법");

                // AlertDialog 셋팅
                alertDialogBuilder1
                        .setMessage("모드버튼은 이렇게 사용합니다")
                        .setCancelable(false)
                        .setPositiveButton("나가기",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {

                                        HowToUseActivity.this.finish();
                                    }
                                })
                        .setNegativeButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });

                // 다이얼로그 생성
                AlertDialog alertDialog1 = alertDialogBuilder1.create();

                // 다이얼로그 보여주기
                alertDialog1.show();
                break;



            case R.id.schedule:

                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
                        context);

                // 제목셋팅
                alertDialogBuilder2.setTitle("스케쥴 사용법");

                // AlertDialog 셋팅
                alertDialogBuilder2
                        .setMessage("스케쥴버튼은 이렇게 사용합니다")
                        .setCancelable(false)
                        .setPositiveButton("나가기",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {

                                        HowToUseActivity.this.finish();
                                    }
                                })
                        .setNegativeButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });

                // 다이얼로그 생성
                AlertDialog alertDialog2 = alertDialogBuilder2.create();

                // 다이얼로그 보여주기
                alertDialog2.show();


                break;
            case R.id.etc:
                AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(
                        context);

                // 제목셋팅
                alertDialogBuilder3.setTitle("기타 사용법");

                // AlertDialog 셋팅
                alertDialogBuilder3
                        .setMessage("기타기능은 이렇게 사용합니다")
                        .setCancelable(false)
                        .setPositiveButton("나가기",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {

                                        HowToUseActivity.this.finish();
                                    }
                                })
                        .setNegativeButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });

                // 다이얼로그 생성
                AlertDialog alertDialog3 = alertDialogBuilder3.create();

                // 다이얼로그 보여주기
                alertDialog3.show();



                break;

            default:
                break;
        }
    }
}
