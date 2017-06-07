package com.example.sjeong.alarmcall;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by kjh on 2017-05-18.
 */

public class HowToUseActivity extends AppCompatActivity implements View.OnClickListener {

    final Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_how_to_use);

        TextView text_mode = (TextView) this.findViewById(R.id.set_text_mode);
        text_mode.setOnClickListener((View.OnClickListener) this);
        TextView text_schedule = (TextView) this.findViewById(R.id.set_text_schedule);
        text_schedule.setOnClickListener((View.OnClickListener) this);
        TextView text_etc = (TextView) this.findViewById(R.id.set_text_etc);
        text_etc.setOnClickListener((View.OnClickListener) this);

        ImageButton mode = (ImageButton) this.findViewById(R.id.set_mode);
        mode.setOnClickListener((View.OnClickListener) this);
        ImageButton schedule = (ImageButton) this.findViewById(R.id.set_schedule);
        schedule.setOnClickListener((View.OnClickListener) this);
        ImageButton etc = (ImageButton) this.findViewById(R.id.set_etc);
        etc.setOnClickListener((View.OnClickListener) this);
        ImageButton exit = (ImageButton) this.findViewById(R.id.exit);
        exit.setOnClickListener((View.OnClickListener) this);

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;

        switch (v.getId()) {
            case R.id.set_text_mode:
            case R.id.set_mode:

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
            case R.id.set_text_schedule:
            case R.id.set_schedule:

                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
                        context);

                // 제목셋팅
                alertDialogBuilder2.setTitle("스케줄 사용법");
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
            case R.id.set_text_etc:
            case R.id.set_etc:
                AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(
                        context);

                // 제목셋팅
                alertDialogBuilder3.setTitle("기타 사용법");

                // AlertDialog 셋팅
                alertDialogBuilder3
                        .setMessage("1.Push Sms 스위치를 On 시키면 모드생성시에 설정해둔 메시지를 차단된 송신자에게 보낼 수 있습니다.\n\n"+"2.Push Later 스위치를 On 하면 부재중 전화를 설정한 시간이후에 팝업창으로 알려줍니다.\n\n" +
                                "3.SENDING EMAIL을 통해 개발자에게 이메일을 보낼 수 있습니다.")
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
            case R.id.exit:
                HowToUseActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
