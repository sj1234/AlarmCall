package com.example.sjeong.alarmcall;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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

import com.tsengvn.typekit.TypekitContextWrapper;

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

       /* Typeface typeface = Typeface.createFromAsset(getAssets(), "bb.ttf");
       TextView textView = (TextView) findViewById(R.id.textView);
        textView.setTypeface(typeface);
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setTypeface(typeface);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setTypeface(typeface);*/

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
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
                        .setMessage("우리 어플은 사용자의 편의대로 여러가지 모드를 만들어 상황에 맞게 사용할 수 있습니다.\n\n1.우측하단의+ 버튼으로 모드를 생성할 수 있습니다.\n\n" +
                                "2.여러개의 모드가 있을 경우 리스트의 전화기 아이콘을 터치해서 활성화/비활성화 할 수 있습니다.\n\n" +
                                "3.현재 활성화된 모드의 정보는 홈 화면에서 확인할 수 있으며 리스트를 다시 터치하면 모드의 정보를 수정할 수 있습니다.")
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
                        .setMessage("이미 생성해둔 모드를 원하는 요일과 시간대에 따라 다르게 활성화 할 수 있게 해주는 기능입니다.\n\n1.우측하단의 + 버튼으로 스케쥴을 생성할 수 있습니다.\n\n" +
                                "2.특정 모드가 원하는 시간대와 요일에 활성화 될 수 있도록 설정할 수 있습니다.\n\n3.리스트의 알람 아이콘을 터치하는 것으로 활성/비활성화 할 수 있습니다.")
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
                        .setMessage("1.Push Later 스위치를 On 하면 부재중 전화를 설정한 시간이후에 팝업창으로 알려줍니다.\n\n\n\n" +
                                "2.SENDING EMAIL을 통해 개발자에게 이메일을 보낼 수 있습니다.")
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
